-- users
INSERT IGNORE INTO users (
    user_id,
    auth0_id,
    point,
    profile_name,
    introduction,
    profile_img_url,
    banner_img_url,
    user_role,
    created_at,
    updated_at
) VALUES
      (1, 'auth0|user001',5000, 'Alice', 'AI 아티스트입니다.', 'https://img.example.com/profile1.jpg', 'https://img.example.com/banner1.jpg', 'creator', NOW(), NOW()),
      (2, 'auth0|user002', 2000, 'Bob',   '프롬프트 수집러입니다.',      'https://img.example.com/profile2.jpg', 'https://img.example.com/banner2.jpg', 'buyer',   NOW(), NOW());

-- model_categories
INSERT IGNORE INTO model_categories (
    model_name,
    model_slug
) VALUES
      ('ChatGPT',     'chatgpt'),
      ('Midjourney',  'midjourney');

-- type_categories
INSERT IGNORE INTO type_categories (
    type_name,
    type_slug
) VALUES
      ('이미지 생성',      'image-generation'),
      ('대화형 AI',        'conversational-ai');

-- prompts
INSERT IGNORE INTO prompts (
    prompt_name,
    prompt_content,
    price,
    ai_inspection_rate,
    owner_id,
    example_content_url,
    created_at,
    updated_at
) VALUES
      ('명화 스타일 초상화 프롬프트', '당신의 사진을 반 고흐 스타일로 변환합니다.',     1000, 'A', 1, 'https://example.com/example1.jpg', NOW(), NOW()),
      ('비즈니스 회의 요약 프롬프트', '회의 내용을 요약해주는 AI 프롬프트',               1500, 'B', 1, 'https://example.com/example2.txt', NOW(), NOW());

-- prompt_classifications
INSERT IGNORE INTO prompt_classifications (
    prompt_id,
    model_id,
    type_id
) VALUES
      (1, 2, 1),  -- Midjourney, 이미지 생성
      (2, 1, 2);  -- ChatGPT, 대화형 AI

-- prompt_purchases
INSERT IGNORE INTO prompt_purchases (
    buyer_id,
    prompt_id,
    purchased_at
) VALUES
      (2, 1, NOW()),
      (2, 2, NOW());

-- user_sales_summary
INSERT IGNORE INTO user_sales_summary (
    user_id,
    summary_date,
    sold_count,
    total_revenue,
    last_updated
) VALUES
    (1, CURDATE(), 2, 2500.00, NOW());

-- user_wishlist
INSERT IGNORE INTO user_wishlist (
    user_id,
    prompt_id,
    added_at
) VALUES
    (2, 1, NOW());

-- prompt_reviews
INSERT IGNORE INTO prompt_reviews (
    purchase_id,
    prompt_id,
    reviewer_id,
    rate,
    review_content,
    reviewed_at,
    updated_at
) VALUES
      (1, 1, 2, 4.5, '멋진 결과물이에요! 추천합니다.', NOW(), NOW()),
      (2, 2, 2, 5.0, '정말 유용합니다. 감사합니다.', NOW(), NOW());
