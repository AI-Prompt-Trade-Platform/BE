-- Users
INSERT IGNORE INTO users (
    auth0_id, point, profile_name, introduction,
    profile_img_url, banner_img_url, user_role,
    created_at, updated_at
) VALUES
      ('auth0|user1',   0, 'Alice', 'Hello, I am Alice',
       '/images/alice.png', '/banners/alice_banner.png', 'USER',
       '2025-06-01 10:00:00', '2025-06-01 10:00:00'),
      ('auth0|user2', 100, 'Bob',   'Hi, Bob here.',
       '/images/bob.png',   '/banners/bob_banner.png',   'USER',
       '2025-06-02 11:00:00', '2025-06-02 11:00:00');

-- Model categories
INSERT IGNORE INTO model_categories (model_name, model_slug) VALUES
                                                          ('GPT-4', 'gpt-4'),
                                                          ('BERT',  'bert');

-- Type categories
INSERT IGNORE INTO type_categories (type_name, type_slug) VALUES
                                                       ('Text Generation',  'text-gen'),
                                                       ('Image Generation', 'image-gen');

-- Tags
INSERT IGNORE INTO tags (name) VALUES
                            ('summary'),
                            ('text'),
                            ('image');

-- Prompts
INSERT IGNORE INTO prompts (
    prompt_name, prompt_content, price,
    ai_inspection_rate, example_content_url,
    description, model, owner_id,
    created_at, updated_at
) VALUES
      ('Summarize Text',
       'Summarize any text into concise bullet points.',
       10, 'A',
       'http://example.com/sample1',
       'Short summary tool',
       '{"model":"gpt-4"}',
       1,
       '2025-06-05 09:00:00', '2025-06-05 09:00:00'
      ),
      ('Generate Image',
       'Create an image based on a text prompt.',
       20, 'B',
       'http://example.com/sample2',
       'AI image creator',
       '{"model":"dall-e"}',
       1,
       '2025-06-06 10:00:00', '2025-06-06 10:00:00'
      ),
      ('테스트용1',
       'ㄷㄱㅎㄷㄱㅎㄷㄱㅍㅈㄷㄱㅍㅈㄷㅍㄷㄱㅍㄷ',
       20, 'B',
       'https://drive.google.com/file/d/188YQaW4ov59u5QLZustsE5OEmb-SiUg4/view?usp=sharing',
       'AI image creator',
       '{"model":"dall-e"}',
       93,
       '2025-06-06 10:00:00', '2025-06-06 10:00:00'
      ),
      ('테스트용2',
       'ㅈㄹㄱㄷㄹㄷㄱㄹㅎㄷㄱㄹ',
       220, 'B',
       'https://drive.google.com/file/d/188YQaW4ov59u5QLZustsE5OEmb-SiUg4/view?usp=sharing',
       'AI image creator',
       '{"model":"dall-e"}',
       93,
       '2025-06-06 10:00:00', '2025-06-06 10:00:00'
      ),
      ('테스트용3',
         's3테스트',
         220, 'B',
         'https://prumpt2-image.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%A1%E1%84%83%E1%85%A9%E1%84%8B%E1%85%B5.jpg',
         'AI image creator',
         '{"model":"dall-e"}',
         93,
         '2025-06-06 10:00:00', '2025-06-06 10:00:00'
      );

-- Prompt classifications
INSERT IGNORE INTO prompt_classifications (prompt_id, model_id, type_id) VALUES
                                                                      (1, 1, 1),
                                                                      (2, 2, 2),
                                                                      (110,2,2);

-- Prompt–Tag associations
INSERT IGNORE INTO prompt_tag (prompt_id, tag_id) VALUES
                                               (1, 1),  -- 'Summarize Text' ↔ 'summary'
                                               (1, 2),  -- 'Summarize Text' ↔ 'text'
                                               (2, 3);  -- 'Generate Image'  ↔ 'image'

-- Prompt purchases
INSERT IGNORE INTO prompt_purchases (buyer_id, prompt_id, purchased_at) VALUES
    (2, 1, '2025-06-07 14:00:00'),
    (7, 2, '2025-06-07 15:00:00'),
    (93, 1, '2025-06-07 14:00:00');

-- User sales summary
INSERT IGNORE INTO user_sales_summary (
    user_id, summary_date, sold_count,
    total_revenue, last_updated
) VALUES
      (1, '2025-06-07', 1, 10.00, '2025-06-07 15:00:00'),
      (1, '2025-06-08', 0,  0.00, '2025-06-08 15:00:00');

-- User wishlist
INSERT IGNORE INTO user_wishlist (user_id, prompt_id, added_at) VALUES
    (2, 2, '2025-06-07 16:00:00');

-- Prompt reviews
INSERT IGNORE INTO prompt_reviews (
    purchase_id, prompt_id, reviewer_id,
    rate, review_content, reviewed_at, updated_at
) VALUES
    (1, 1, 2, 4.5, 'Great summary!', '2025-06-07 15:30:00', '2025-06-07 15:30:00');

