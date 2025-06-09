-- 사용자 테이블
CREATE TABLE IF NOT EXISTS `users` (
                      `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '유저 고유 ID',
                       auth0_id VARCHAR(255) NOT NULL UNIQUE COMMENT 'Auth0 고유 식별자',
                       point INT NOT NULL DEFAULT 0 COMMENT '포인트',
                       profile_name VARCHAR(255) NOT NULL COMMENT '프로필 이름',
                       introduction TEXT COMMENT '자기소개',
                       profile_img_url VARCHAR(255) COMMENT '프로필 이미지 URL',
                       banner_img_url VARCHAR(255) COMMENT '배너 이미지 URL',
                       user_role VARCHAR(255) NOT NULL COMMENT '유저 역할',
                       created_at DATETIME NOT NULL COMMENT '계정 생성 시각',
                       updated_at DATETIME COMMENT '계정 정보 수정 시각'
) COMMENT='유저 정보 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 모델 카테고리 이름 테이블
CREATE TABLE IF NOT EXISTS  `model_categories` (
                                    `model_id`   INT          NOT NULL AUTO_INCREMENT COMMENT '필수: 카테고리 PK',
                                    `model_name` VARCHAR(50)  NOT NULL                COMMENT '필수: AI모델 이름',
                                    `model_slug` VARCHAR(50)  NOT NULL                COMMENT '필수: URL 식별자',
                                    PRIMARY KEY (`model_id`),
                                    UNIQUE KEY `uniq_category_model_slug` (`model_slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 종류 카테고리 이름 테이블
CREATE TABLE IF NOT EXISTS  `type_categories` (
                                   `type_id`   INT          NOT NULL AUTO_INCREMENT COMMENT '필수: 카테고리 PK',
                                   `type_name` VARCHAR(50)  NOT NULL                COMMENT '필수: 프롬프트 종류 이름',
                                   `type_slug` VARCHAR(50)  NOT NULL                COMMENT '필수: URL 식별자',
                                   PRIMARY KEY (`type_id`),
                                   UNIQUE KEY `uniq_category_type_slug` (`type_slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 프롬프트 테이블
CREATE TABLE IF NOT EXISTS  prompts (
                         prompt_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프롬프트 고유 ID',
                         prompt_name VARCHAR(255) NOT NULL COMMENT '프롬프트 이름',
                         prompt_content TEXT COMMENT '프롬프트 내용',
                         price INT COMMENT '프롬프트 가격',
                         ai_inspection_rate VARCHAR(255) COMMENT 'AI 검수 등급',
                         owner_id INT NOT NULL COMMENT '프롬프트 소유자(유저) ID',
                         example_content_url VARCHAR(255) COMMENT '예시 콘텐츠 URL',
                         created_at DATETIME NOT NULL COMMENT '생성 시각',
                         updated_at DATETIME COMMENT '수정 시각',
                         model TEXT COMMENT 'AI 모델 정보',
                         INDEX idx_prompts_owner_id       (owner_id),
                         INDEX idx_prompts_created_at     (created_at),
                         UNIQUE (prompt_name, owner_id),
                         CONSTRAINT fk_prompts_owner
                             FOREIGN KEY (owner_id) REFERENCES users(user_id)
) COMMENT='프롬프트 정보 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 프롬프트 분류 테이블
CREATE TABLE IF NOT EXISTS  `prompt_classifications` (
                                          `prompt_id`         INT NOT NULL COMMENT '필수: 프롬프트 ID',
                                          `model_id` INT NOT NULL COMMENT '필수: 모델 카테고리 ID',
                                          `type_id`  INT NOT NULL COMMENT '필수: 타입 카테고리 ID',
                                          PRIMARY KEY (`prompt_id`,`model_id`,`type_id`),
                                          CONSTRAINT `fk_pc_prompt` FOREIGN KEY (`prompt_id`) REFERENCES `prompts`(`prompt_id`) ON DELETE CASCADE,
                                          CONSTRAINT `fk_pc_model_category` FOREIGN KEY (`model_id`) REFERENCES `model_categories`(`model_id`) ON DELETE CASCADE,
                                          CONSTRAINT `fk_pc_type_category` FOREIGN KEY (`type_id`) REFERENCES `type_categories`(`type_id`) ON DELETE CASCADE,
                                          INDEX idx_pc_model_id  (model_id),
                                          INDEX idx_pc_type_id   (type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 거래 내역 테이블
CREATE TABLE IF NOT EXISTS  prompt_purchases (
                                  purchase_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프롬프트 구매 고유 ID',
                                  buyer_id INT NOT NULL COMMENT '구매자(유저) ID (외래키)',
                                  prompt_id INT NOT NULL COMMENT '구매한 프롬프트 ID (외래키)',
                                  purchased_at DATETIME NOT NULL COMMENT '구매 시각',
                                  INDEX idx_purchases_buyer_id     (buyer_id),
                                  INDEX idx_purchases_prompt_id    (prompt_id),
                                  INDEX idx_purchases_purchased_at (purchased_at),
                                  CONSTRAINT fk_purchases_buyer
                                      FOREIGN KEY (buyer_id) REFERENCES users(user_id),
                                  CONSTRAINT fk_purchases_prompt
                                      FOREIGN KEY (prompt_id) REFERENCES prompts(prompt_id)
) COMMENT='프롬프트 구매 내역 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 수익 요약 테이블
CREATE TABLE IF NOT EXISTS  user_sales_summary (
                                    user_id      INT        NOT NULL COMMENT '유저 고유 ID (users 테이블과 매핑)',
                                    summary_date DATE       NOT NULL COMMENT '집계 대상 날짜',
                                    sold_count   INT        NOT NULL COMMENT '당일 판매 건수',
                                    total_revenue DECIMAL(19,2) NOT NULL COMMENT '당일 총 매출',
                                    last_updated DATETIME   NOT NULL COMMENT '마지막 업데이트 시각',
                                    PRIMARY KEY (user_id, summary_date),
                                    CONSTRAINT fk_user_sales_summary_user
                                        FOREIGN KEY (user_id) REFERENCES users(user_id),

                                INDEX idx_summary_summary_date (summary_date)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='유저별 일일 판매 요약 테이블';


-- 위시리스트 테이블
CREATE TABLE IF NOT EXISTS  `user_wishlist` (
                                 `wishlist_id`   INT            NOT NULL AUTO_INCREMENT COMMENT '필수: 위시리스트 ID',
                                 `user_id`       INT            NOT NULL COMMENT '필수: 사용자 ID',
                                 `prompt_id`     INT            NOT NULL COMMENT '필수: 프롬프트 ID',
                                 `added_at`      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '필수: 추가 시각',
                                 PRIMARY KEY (`wishlist_id`),
                                 UNIQUE KEY `uk_user_prompt_wish` (`user_id`,`prompt_id`),
                                 FOREIGN KEY (`user_id`)   REFERENCES users(user_id) ON DELETE CASCADE,
                                 FOREIGN KEY (`prompt_id`) REFERENCES `prompts`(`prompt_id`) ON DELETE CASCADE,
                                 INDEX idx_wishlist_user_id    (user_id),
                                 INDEX idx_wishlist_prompt_id  (prompt_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 후기 테이블
CREATE TABLE IF NOT EXISTS  prompt_reviews (
                                review_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프롬프트 리뷰 고유 ID',
                                purchase_id INT NOT NULL UNIQUE COMMENT '구매 내역 ID (외래키, 1:1)',
                                prompt_id INT NOT NULL COMMENT '프롬프트 ID (외래키, N:1)',
                                reviewer_id INT NOT NULL COMMENT '리뷰 작성자 ID (외래키, N:1)',
                                rate DOUBLE COMMENT '리뷰 평점',
                                review_content VARCHAR(255) COMMENT '리뷰 내용',
                                reviewed_at DATETIME COMMENT '리뷰 작성 시각',
                                updated_at DATETIME COMMENT '리뷰 수정 시각',
                                CONSTRAINT fk_prompt_reviews_purchase
                                    FOREIGN KEY (purchase_id) REFERENCES prompt_purchases(purchase_id),
                                CONSTRAINT fk_prompt_reviews_prompt
                                    FOREIGN KEY (prompt_id) REFERENCES prompts(prompt_id),
                                CONSTRAINT fk_prompt_reviews_reviewer
                                    FOREIGN KEY (reviewer_id) REFERENCES users(user_id),
                                INDEX idx_reviews_prompt_id    (prompt_id),
                                INDEX idx_reviews_reviewed_at  (reviewed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='프롬프트 리뷰 테이블';