package com.oh.nbcam_ch4.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oh.nbcam_ch4.common.constant.S3KeyPrefix;
import com.oh.nbcam_ch4.common.exception.CustomException;
import com.oh.nbcam_ch4.common.exception.ErrorCode;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class S3Service {

	private static final Duration EXPIRATION_TIME = Duration.ofMinutes(10);
	private final String bucket;
	private final S3Template s3Template;

	public S3Service(@Value("${spring.cloud.aws.s3.bucket}") String bucket, S3Template s3Template) {
		this.bucket = bucket;
		this.s3Template = s3Template;
	}

	// TODO: 파일 형식 검증
	public String upload(MultipartFile file, S3KeyPrefix keyPrefix) {
		// UUID를 통해 키 중복 방지
		String key = keyPrefix.getPrefix() + UUID.randomUUID() + "_" + file.getOriginalFilename();
		ObjectMetadata metadata = ObjectMetadata.builder().contentType(file.getContentType()).build();
		try {
			s3Template.upload(bucket, key, file.getInputStream(), metadata);
			log.info("S3 업로드 완료 key = {}", key);
			return key;

		} catch (Exception e) {
			log.error("S3 업로드 실패 key = {}", key, e);
			throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}

	public String getDownloadURL(String key) {
		return s3Template.createSignedGetURL(bucket, key, EXPIRATION_TIME).toString();
	}

	/* DB에 key 업데이트 실패 시 롤백 전에
	   S3에 저장한 새로운 파일도 지워주기 위해 호출 (고아 객체 지우기 위함)

	   실패 시 고아 객체 판별을 위해 로깅 */
	public void delete(String key) {
		try {
			s3Template.deleteObject(bucket, key);

		} catch (Exception e) {
			log.error("S3 삭제 실패 key = {}", key, e);
		}
	}
}