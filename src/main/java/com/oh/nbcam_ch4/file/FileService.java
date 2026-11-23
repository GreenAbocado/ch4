package com.oh.nbcam_ch4.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oh.nbcam_ch4.common.exception.CustomException;
import com.oh.nbcam_ch4.common.exception.ErrorCode;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {

	private final String bucket;
	private final S3Template s3Template;
	private final String cloudFrontDomain;
	private final Tika tika = new Tika();

	public FileService(@Value("${spring.cloud.aws.s3.bucket}") String bucket,
		S3Template s3Template,
		@Value("${spring.cloud.aws.cloudfront.domain}") String cloudFrontDomain) {

		this.bucket = bucket;
		this.s3Template = s3Template;
		this.cloudFrontDomain = cloudFrontDomain;
	}

	public String upload(MultipartFile file, S3KeyPrefix keyPrefix) {

		FileType fileType = extractFileType(file);

		// UUID를 통해 중복 없는 파일명 생성, 서버에서 파일 형식 검증 후 직접 확장자, Content-Type 세팅
		String key = keyPrefix.getPrefix() + UUID.randomUUID() + fileType.getExtension();
		ObjectMetadata metadata = ObjectMetadata.builder()
				.contentType(fileType.getContentType())
				.build();

		try (InputStream in = file.getInputStream()) {
			s3Template.upload(bucket, key, in, metadata);
			log.info("S3 업로드 완료 key = {}", key);
			return key;

		} catch (Exception e) {
			log.error("S3 업로드 실패 key = {}", key, e);
			throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}

	private FileType extractFileType(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new CustomException(ErrorCode.REQUEST_FILE_EMPTY);
		}

		try (InputStream in = file.getInputStream()) {
			return FileType.fromContentType(tika.detect(in));
		} catch (IOException e) {
			throw new UncheckedIOException("파일 읽기 실패", e);
		}
	}

	public String getDownloadURL(String key) {
		return cloudFrontDomain + "/" + key;
	}

	/* 이미지 정리용 메서드 (profileImgKey 커밋 성공 시 기존 이미지 제거, 롤백 시 새로운 이미지 제거)
	   실패 시 고아 객체 판별을 위해 로깅 */
	public void delete(String key) {
		try {
			s3Template.deleteObject(bucket, key);

		} catch (Exception e) {
			log.error("S3 삭제 실패 key = {}", key, e);
		}
	}
}