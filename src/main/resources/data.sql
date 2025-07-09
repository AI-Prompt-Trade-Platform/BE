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
                                                          ('GPT-4o', 'gpt-4o'),
                                                          ('Midjourney','Midjourney'),
                                                          ('gemini 2.5 Pro','gemini 2.5 Pro')
;

-- Type categories
INSERT IGNORE INTO type_categories (type_name, type_slug) VALUES
                                                       ('텍스트',  'text-generation'),
                                                       ('이미지', 'image-generation'),
                                                       ('영상', 'video-generation');

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
      ('사이버펑크 도시의 밤 풍경', '네온사인이 빛나는 미래 도시의 밤, 비 내리는 길거리를 걷는 탐정의 뒷모습을 그려줘. 블레이드 러너 스타일로.', 4900, '[S][스타일과 분위기 묘사가 구체적임]', 'https://picsum.photos/seed/prompt1/800/600', '비 내리는 미래 도시의 밤', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('귀여운 고양이 캐릭터 5종 세트', '다양한 직업을 가진 귀여운 고양이 캐릭터 5개를 디자인해줘. (의사, 요리사, 우주비행사, 화가, 소방관)', 9900, '[S][요구사항이 명확하고 상업적 활용도가 높음]', 'https://picsum.photos/seed/prompt2/800/600', '직업별 고양이 캐릭터 디자인', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('블로그 포스팅 자동 작성기', '최신 IT 기술 트렌드에 대한 1500자 분량의 전문적인 블로그 포스트를 작성해줘. 주제는 "양자 컴퓨팅의 현재와 미래"야.', 3500, '[B][주제는 좋으나, 타겟 독자층이 명시되지 않음]', 'https://picsum.photos/seed/prompt3/800/600', 'IT 트렌드 블로그 포스팅', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('수채화 느낌의 풍경화', '고요한 호숫가에 있는 작은 오두막의 새벽 풍경을 수채화 스타일로 그려줘. 물안개가 피어오르는 느낌을 강조해줘.', 5500, '[A][감성적 표현과 기법이 구체적임]', 'https://picsum.photos/seed/prompt4/800/600', '고요한 호숫가 오두막', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('유튜브 쇼츠 대본 생성', '일상 속 유용한 꿀팁 3가지를 소개하는 1분짜리 유튜브 쇼츠 영상 대본을 작성해줘. 경쾌하고 빠른 템포로.', 2000, '[A][플랫폼과 길이에 맞는 구체적인 요구]', 'https://picsum.photos/seed/prompt5/800/600', '1분 꿀팁 쇼츠 대본', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('Python으로 웹 스크레이퍼 만들기', 'Python의 BeautifulSoup 라이브러리를 사용해서 특정 뉴스 사이트의 헤드라인 기사 제목을 모두 긁어오는 코드 스크립트를 작성해줘.', 7000, '[D][대상 사이트 URL이 명시되지 않아 범용성이 떨어짐]', 'https://picsum.photos/seed/prompt6/800/600', '파이썬 웹 스크레이핑 코드', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('음식 사진 보정 스타일', '평범한 파스타 사진을 인스타그램 맛집처럼 보이도록 보정해줘. 따뜻한 색감과 높은 채도를 사용하고, 배경을 살짝 흐리게 만들어줘.', 4000, '[S][명확한 레퍼런스와 후보정 가이드 제시]', 'https://picsum.photos/seed/prompt7/800/600', '인스타 감성 음식 사진', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('게임 아이템 아이콘 제작', '판타지 RPG 게임에 등장할 전설 등급 검 아이템 아이콘을 2D 스타일로 디자인해줘. 불과 용의 형상을 모티브로.', 12000, '[S][장르, 등급, 모티브가 명확함]', 'https://picsum.photos/seed/prompt8/800/600', '판타지 게임 아이템 아이콘', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('이메일 자동 응답 템플릿', '고객 문의에 대한 1차 자동 응답 이메일 템플릿을 작성해줘. 문의 접수 사실과 예상 답변 시간을 안내하는 내용 포함.', 1500, '[C][내용은 좋으나, 존댓말/반말 등 톤앤매너 지정이 없음]', 'https://picsum.photos/seed/prompt9/800/600', '고객 문의 자동응답 메일', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('로고 디자인 컨셉 제안', '친환경 유기농 펫푸드 브랜드 "Green Paw"의 로고 디자인 컨셉을 3가지 제안해줘. 자연, 건강, 신뢰의 키워드를 담아서.', 8000, '[A][브랜드명과 핵심 키워드가 명확함]', 'https://picsum.photos/seed/prompt10/800/600', '친환경 펫푸드 로고 컨셉', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('픽셀 아트 배경 이미지', '해질녘 노을이 지는 바닷가를 16비트 게임 스타일의 픽셀 아트로 그려줘. 파도가 부드럽게 움직이는 느낌으로.', 6500, '[A][아트 스타일과 동적 요소가 구체적임]', 'https://picsum.photos/seed/prompt11/800/600', '해질녘 바다 픽셀 아트', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('SQL 쿼리 최적화', '느리게 동작하는 `SELECT * FROM users WHERE last_login < ?` 형태의 SQL 쿼리를 최적화할 수 있는 인덱스 생성 및 쿼리 수정 방안을 제시해줘.', 5000, '[C][상황은 명확하나, 테이블 스키마 전체 정보가 없음]', 'https://picsum.photos/seed/prompt12/800/600', 'SQL 쿼리 성능 최적화', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('동화책 삽화 생성', '"달나라로 간 토끼" 이야기의 한 장면을 동화책 삽화 스타일로 그려줘. 토끼가 별사탕을 따는 모습을 사랑스럽게 표현해줘.', 4500, '[A][대상 연령층과 장면 묘사가 구체적임]', 'https://picsum.photos/seed/prompt13/800/600', '달나라 토끼 동화 삽화', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('광고 카피라이팅', '20대 여성을 타겟으로 하는 새로운 향수 "Starlight"의 인스타그램 광고 문구를 5가지 버전으로 작성해줘. "꿈꾸는 순간의 향기"를 컨셉으로.', 3000, '[S][타겟, 제품명, 컨셉이 모두 명확함]', 'https://picsum.photos/seed/prompt14/800/600', '여성 향수 광고 카피', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('3D 렌더링용 자동차 모델링', '클래식 스포츠카인 포르쉐 911 (1980년식)을 3D 렌더링할 수 있도록 상세하게 묘사해줘. 특히 헤드라이트와 휠의 디테일을 강조해서.', 15000, '[B][모델은 명확하나, 최종 렌더링 스타일 지정이 없음]', 'https://picsum.photos/seed/prompt15/800/600', '클래식카 3D 모델링', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('반 고흐 스타일의 자화상', '나의 사진을 반 고흐의 "별이 빛나는 밤" 스타일로 재해석해서 그려줘. 강렬한 붓 터치와 소용돌이치는 배경을 포함해서.', 7500, '[A][유명 화가 스타일 모방은 인기있는 주제임]', 'https://picsum.photos/seed/prompt16/800/600', '반 고흐풍 자화상', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('면접 예상 질문 리스트업', 'IT 기업의 백엔드 개발자 (신입) 직무 면접에서 나올 가능성이 높은 기술 면접 질문 20개와 모범 답안을 정리해줘.', 4000, '[A][구체적인 직무와 상황에 대한 요구사항]', 'https://picsum.photos/seed/prompt17/800/600', '백엔드 개발자 면접 질문', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('여행 계획 전문가', '3박 4일 일정으로 떠나는 일본 도쿄 여행 계획을 세워줘. 항공, 숙소는 제외하고, 신주쿠/시부야 중심의 맛집과 관광지 추천 포함.', 2500, '[B][지역이 명시되었으나, 여행자의 취향이 없음]', 'https://picsum.photos/seed/prompt18/800/600', '3박 4일 도쿄 여행 계획', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('영화 포스터 디자인', '외계인이 침공하는 SF 블록버스터 영화 "The Last Signal"의 포스터를 디자인해줘. 파괴되는 도시와 거대한 우주선이 보이도록.', 8800, '[S][장르와 시각적 요소가 매우 구체적임]', 'https://picsum.photos/seed/prompt19/800/600', 'SF 영화 포스터 디자인', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('명상 가이드 음성 대본', '불면증으로 잠 못 이루는 사람들을 위한 10분짜리 명상 가이드 음성 대본을 작성해줘. 차분하고 안정적인 톤으로.', 1800, '[B][주제는 좋으나, 배경 음악 등에 대한 언급이 없음]', 'https://picsum.photos/seed/prompt20/800/600', '숙면 유도 명상 대본', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('지브리 스튜디오풍 애니메이션 배경', '미야자키 하야오 감독의 "이웃집 토토로"에 나올 법한 시골 여름 풍경을 그려줘. 푸른 논과 뭉게구름이 있는 하늘.', 9200, '[S][특정 스타일과 감성을 정확히 지목함]', 'https://picsum.photos/seed/prompt21/800/600', '지브리 스타일 시골 풍경', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('Java 코드 리팩토링', '아래의 중첩된 if-else 문을 stream과 lambda를 사용하여 더 간결하고 읽기 좋은 코드로 리팩토링 해줘. (코드 첨부 필요)', 6000, '[C][리팩토링 대상 코드가 없어 즉시 사용 불가]', 'https://picsum.photos/seed/prompt22/800/600', 'Java 코드 리팩토링', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('MBTI 유형별 추천 직업', 'MBTI 성격 유형 중 INFP에게 가장 잘 어울리는 직업 5가지를 추천하고, 그 이유를 상세하게 설명해줘.', 2200, '[A][명확한 타겟과 결과물 형태 제시]', 'https://picsum.photos/seed/prompt23/800/600', 'INFP 추천 직업', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('웹툰 캐릭터 시트', '학원 로맨스 웹툰의 남자 주인공 캐릭터 시트를 만들어줘. 까칠하지만 속은 따뜻한 성격. 교복과 사복 버전 포함.', 7800, '[A][장르, 성격, 구성요소가 명확함]', 'https://picsum.photos/seed/prompt24/800/600', '웹툰 남자 주인공 시트', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('데이터 분석 보고서 초안 작성', '월간 사용자 접속 데이터를 기반으로 이탈률이 높은 페이지를 분석하고, 원인과 개선 방안을 담은 보고서 초안을 작성해줘.', 5500, '[B][목적은 명확하나, 원본 데이터가 없어 일반적인 내용만 생성 가능]', 'https://picsum.photos/seed/prompt25/800/600', '데이터 분석 보고서 초안', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('강아지 견종 맞추기', '사진 속 강아지가 어떤 품종인지 알려줘.', 1000, '[D][질문이 너무 단순하고 구체적인 정보가 없음]', 'https://picsum.photos/seed/prompt26/800/600', '강아지 품종 식별', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('우주를 여행하는 고래', '은하수를 배경으로 거대한 고래가 유영하는 모습을 신비롭고 몽환적인 스타일로 그려줘.', 11000, '[S][독창적이고 창의적인 소재와 분위기]', 'https://picsum.photos/seed/prompt27/800/600', '우주 고래 일러스트', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('단편 소설 아이디어', '시간 여행을 소재로 한 반전 있는 단편 소설의 시놉시스를 500자 내외로 작성해줘.', 2800, '[A][장르와 핵심 소재, 분량까지 명시됨]', 'https://picsum.photos/seed/prompt28/800/600', '시간 여행 소설 시놉시스', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('타투 도안 디자인', '장미와 뱀을 주제로 한 블랙워크 스타일의 타투 도안을 그려줘. 팔뚝 안쪽에 어울리는 세로가 긴 형태로.', 8500, '[A][스타일, 주제, 위치가 구체적임]', 'https://picsum.photos/seed/prompt29/800/600', '장미와 뱀 타투 도안', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('프레젠테이션 디자인', '투자 유치를 위한 10페이지 분량의 IR 프레젠테이션 템플릿을 디자인해줘. 깔끔하고 전문적인 느낌으로. 파란색을 메인 컬러로 사용.', 9500, '[B][목적은 명확하나, 회사 로고나 구체적 내용이 없음]', 'https://picsum.photos/seed/prompt30/800/600', 'IR 프레젠테이션 템플릿', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('해변의 석양', '아름다운 해변의 석양을 유화 스타일로 그려줘. 붉게 물든 하늘과 바다의 반사를 강조해서.', 5800, '[A][보편적이지만 구체적인 스타일 요구]', 'https://picsum.photos/seed/prompt31/800/600', '유화 스타일 해변 석양', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('레시피 생성', '냉장고에 있는 재료(닭가슴살, 양파, 계란, 우유)만으로 만들 수 있는 간단한 저녁 메뉴 레시피를 알려줘.', 1200, '[B][재료는 명시했으나, 요리 스타일(한식, 양식 등)이 없음]', 'https://picsum.photos/seed/prompt32/800/600', '냉장고 재료 레시피', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('JavaScript ES6 문법 퀴즈', 'JavaScript의 ES6 신규 문법(let/const, arrow function, destructuring)에 대한 간단한 퀴즈 5개를 만들어줘.', 3200, '[A][주제와 결과물 형태가 명확함]', 'https://picsum.photos/seed/prompt33/800/600', 'JavaScript ES6 퀴즈', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('중세시대 갑옷 디자인', '중세 판타지에 등장하는 여기사의 은색 갑옷 세트를 디자인해줘. 우아하면서도 실용적인 디자인으로.', 13000, '[A][컨셉과 디자인 방향성이 뚜렷함]', 'https://picsum.photos/seed/prompt34/800/600', '판타지 여기사 갑옷', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('운동 루틴 추천', '체지방 감량을 목표로 하는 직장인을 위한 주 3회 헬스장 운동 루틴을 짜줘. (월: 가슴/삼두, 수: 등/이두, 금: 하체/어깨)', 4200, '[S][타겟, 목표, 빈도, 부위 분할까지 완벽함]', 'https://picsum.photos/seed/prompt35/800/600', '헬스 다이어트 루틴', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('인테리어 디자인 아이디어', '10평 원룸을 넓어 보이게 꾸밀 수 있는 인테리어 아이디어를 3가지 제안해줘. 화이트톤과 미니멀리즘을 컨셉으로.', 6800, '[A][공간, 평수, 컨셉이 명확함]', 'https://picsum.photos/seed/prompt36/800/600', '10평 원룸 인테리어', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('공포 영화 시놉시스', '오래된 폐병원에 들어간 유튜버들의 라이브 방송이 진짜 공포로 변하는 내용의 파운드 푸티지 영화 시놉시스를 작성해줘.', 3800, '[S][장르, 소재, 스타일이 매우 구체적임]', 'https://picsum.photos/seed/prompt37/800/600', '폐병원 공포 영화 시놉시스', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('음악 작곡', '비 오는 날 카페에서 듣기 좋은 Lo-fi 재즈 피아노 연주곡을 작곡해줘. 4/4박자, 80bpm으로.', 9000, '[A][장르, 분위기, 박자, 빠르기까지 구체적]', 'https://picsum.photos/seed/prompt38/800/600', '비 오는 날 Lo-fi 재즈', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('브랜드 네이밍', '20대 남성을 위한 온라인 패션 쇼핑몰 이름을 10개 제안해줘. 트렌디하고 기억하기 쉬운 이름으로.', 2500, '[B][타겟은 명확하나, 패션 스타일(스트릿, 미니멀 등)이 없음]', 'https://picsum.photos/seed/prompt39/800/600', '남성 패션몰 이름 제안', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('고양이 그리기', '고양이 그려줘', 500, '[D][요구가 너무 추상적이고 불분명함]', 'https://picsum.photos/seed/prompt40/800/600', '고양이 그림', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('스티커용 SD 캐릭터', '판매용 스티커로 제작할 수 있는 귀여운 공룡 SD 캐릭터를 그려줘. 단순한 라인과 밝은 색상을 사용해서.', 6200, '[A][용도와 스타일이 명확함]', 'https://picsum.photos/seed/prompt41/800/600', '공룡 SD 캐릭터 스티커', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('React 커스텀 훅 만들기', 'React에서 API 호출 상태(loading, data, error)를 쉽게 관리할 수 있는 `useFetch` 커스텀 훅 코드를 작성해줘.', 7200, '[S][매우 실용적이고 재사용성 높은 코드 요구]', 'https://picsum.photos/seed/prompt42/800/600', 'React useFetch 훅 코드', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('전래동화 다시 쓰기', '흥부와 놀부 이야기를 현대적으로 재해석해서, 놀부가 사실은 시대를 앞서간 투자자였다는 설정의 단편 소설을 써줘.', 4800, '[A][독창적인 재해석 방향 제시]', 'https://picsum.photos/seed/prompt43/800/600', '현대판 흥부와 놀부', 'Gemini 2.5 Pro', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('포토카드 디자인', 'K-POP 아이돌의 포토카드 앞면과 뒷면을 디자인해줘. 앞면은 클로즈업 사진, 뒷면은 손글씨와 사인이 들어가도록.', 8200, '[B][사진이나 사인 등 원본 소스가 없음]', 'https://picsum.photos/seed/prompt44/800/600', '아이돌 포토카드 디자인', 'Midjourney', FLOOR(1 + RAND() * 3), NOW(), NOW()),
      ('기술 문서 번역', '영어로 작성된 API 문서를 자연스러운 한국어로 번역해줘. 개발자들이 이해하기 쉬운 용어를 사용해서.', 5200, '[C][번역 대상 문서가 없어 바로 사용 불가]', 'https://picsum.photos/seed/prompt45/800/600', 'API 기술 문서 번역', 'GPT-4o', FLOOR(1 + RAND() * 3), NOW(), NOW());


-- Prompt classifications
INSERT IGNORE INTO prompt_classifications (prompt_id, model_id, type_id) VALUES
                                                                             (1, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (2, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (3, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (4, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (5, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (6, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (7, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (8, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (9, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (10, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (11, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (12, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (13, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (14, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (15, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (16, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (17, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (18, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (19, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (20, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (21, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (22, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (23, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (24, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (25, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (26, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (27, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (28, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (29, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (30, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (31, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (32, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (33, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (34, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (35, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (36, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (37, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (38, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (39, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (40, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (41, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (42, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (43, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (44, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3)),
                                                                             (45, FLOOR(1 + RAND() * 3), FLOOR(1 + RAND() * 3));

-- Prompt–Tag associations
INSERT IGNORE INTO prompt_tag (prompt_id, tag_id) VALUES
                                               (1, 1),  -- 'Summarize Text' ↔ 'summary'
                                               (1, 2),  -- 'Summarize Text' ↔ 'text'
                                               (2, 3);  -- 'Generate Image'  ↔ 'image'

-- Prompt purchases
INSERT IGNORE INTO prompt_purchases (buyer_id, prompt_id, purchased_at) VALUES
                                                                            (2, 22, '2025-06-08 10:00:12'),
                                                                            (1, 8, '2025-06-08 11:00:54'),
                                                                            (3, 41, '2025-06-08 11:50:23'),
                                                                            (1, 15, '2025-06-08 13:30:45'),
                                                                            (2, 33, '2025-06-08 13:55:01'),
                                                                            (3, 5, '2025-06-09 09:15:11'),
                                                                            (1, 28, '2025-06-09 09:45:33'),
                                                                            (2, 19, '2025-06-09 11:40:19'),
                                                                            (3, 44, '2025-06-09 14:05:05'),
                                                                            (1, 2, '2025-06-10 10:01:20'),
                                                                            (2, 37, '2025-06-10 10:45:00'),
                                                                            (3, 11, '2025-06-10 13:00:00'),
                                                                            (1, 25, '2025-06-11 14:50:29'),
                                                                            (2, 4, '2025-06-11 16:10:15'),
                                                                            (3, 30, '2025-06-11 17:50:00'),
                                                                            (1, 17, '2025-06-12 09:30:18'),
                                                                            (2, 42, '2025-06-12 11:05:32'),
                                                                            (3, 9, '2025-06-12 13:45:00'),
                                                                            (1, 21, '2025-06-13 10:15:43'),
                                                                            (2, 38, '2025-06-13 12:20:00'),
                                                                            (3, 13, '2025-06-13 15:10:10'),
                                                                            (1, 45, '2025-06-14 10:55:00'),
                                                                            (2, 7, '2025-06-14 13:33:33'),
                                                                            (3, 32, '2025-06-14 15:45:00'),
                                                                            (1, 18, '2025-06-15 09:50:00'),
                                                                            (2, 29, '2025-06-15 12:00:00'),
                                                                            (3, 3, '2025-06-15 14:25:00'),
                                                                            (1, 36, '2025-06-16 09:40:00'),
                                                                            (2, 12, '2025-06-16 11:25:15'),
                                                                            (3, 26, '2025-06-16 16:30:00');

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
INSERT IGNORE INTO prompt_reviews (purchase_id, prompt_id, reviewer_id, rate, review_content, reviewed_at, updated_at) VALUES
                                                                                                                           (1, 22, 1, 4.5, '정말 훌륭한 프롬프트입니다!', '2025-06-08 10:15:00', '2025-06-08 10:15:00'),
                                                                                                                           (2, 8, 3, 3.0, '조금 더 구체적이었으면 좋겠습니다.', '2025-06-08 11:20:30', '2025-06-08 11:20:30'),
                                                                                                                           (3, 41, 2, 5.0, '최고의 결과물을 얻었어요. 감사합니다.', '2025-06-08 12:05:10', '2025-06-08 12:05:10'),
                                                                                                                           (4, 15, 1, 4.0, '유용하게 잘 사용했습니다.', '2025-06-08 13:45:00', '2025-06-08 13:45:00'),
                                                                                                                           (5, 33, 2, 2.5, '기대했던 것과는 조금 다르네요.', '2025-06-08 14:00:15', '2025-06-08 14:00:15'),
                                                                                                                           (6, 5, 3, 4.5, '시간을 많이 절약했습니다.', '2025-06-09 09:30:00', '2025-06-09 09:30:00'),
                                                                                                                           (7, 28, 1, 3.5, '괜찮은 시작점입니다.', '2025-06-09 10:00:45', '2025-06-09 10:00:45'),
                                                                                                                           (8, 19, 2, 1.5, '거의 도움이 되지 않았습니다.', '2025-06-09 11:55:00', '2025-06-09 11:55:00'),
                                                                                                                           (9, 44, 3, 5.0, '완벽해요! 강력 추천합니다.', '2025-06-09 14:20:00', '2025-06-09 14:20:00'),
                                                                                                                           (10, 2, 1, 4.0, '결과가 만족스럽습니다.', '2025-06-10 10:10:10', '2025-06-10 10:10:10'),
                                                                                                                           (11, 37, 2, 2.0, '설명이 부족합니다.', '2025-06-10 11:00:00', '2025-06-10 11:00:00'),
                                                                                                                           (12, 11, 3, 4.5, '생각보다 훨씬 좋네요!', '2025-06-10 13:15:30', '2025-06-10 13:15:30'),
                                                                                                                           (13, 25, 1, 3.5, '나쁘지 않습니다.', '2025-06-11 15:00:00', '2025-06-11 15:00:00'),
                                                                                                                           (14, 4, 2, 5.0, '제가 찾던 바로 그 프롬프트입니다!', '2025-06-11 16:30:00', '2025-06-11 16:30:00'),
                                                                                                                           (15, 30, 3, 2.5, '조금 실망스럽습니다.', '2025-06-11 18:00:45', '2025-06-11 18:00:45'),
                                                                                                                           (16, 17, 1, 4.0, '아주 유용합니다.', '2025-06-12 09:45:00', '2025-06-12 09:45:00'),
                                                                                                                           (17, 42, 2, 3.0, '평범한 수준입니다.', '2025-06-12 11:10:00', '2025-06-12 11:10:00'),
                                                                                                                           (18, 9, 3, 5.0, '놀라운 퀄리티!', '2025-06-12 14:00:00', '2025-06-12 14:00:00'),
                                                                                                                           (19, 21, 1, 1.0, '전혀 작동하지 않아요.', '2025-06-13 10:25:00', '2025-06-13 10:25:00'),
                                                                                                                           (20, 38, 2, 4.5, '작업 효율이 크게 올랐습니다.', '2025-06-13 12:30:00', '2025-06-13 12:30:00'),
                                                                                                                           (21, 13, 3, 3.5, '그럭저럭 쓸만합니다.', '2025-06-13 15:20:30', '2025-06-13 15:20:30'),
                                                                                                                           (22, 45, 1, 4.0, '좋은 퀄리티의 결과물을 얻었습니다.', '2025-06-14 11:00:00', '2025-06-14 11:00:00'),
                                                                                                                           (23, 7, 2, 2.5, '아이디어가 좋지만, 개선이 필요합니다.', '2025-06-14 13:40:00', '2025-06-14 13:40:00'),
                                                                                                                           (24, 32, 3, 4.5, '쉽고 빠르게 사용할 수 있었어요.', '2025-06-14 16:00:00', '2025-06-14 16:00:00'),
                                                                                                                           (25, 18, 1, 5.0, '기대 이상입니다. 훌륭해요.', '2025-06-15 10:00:00', '2025-06-15 10:00:00'),
                                                                                                                           (26, 29, 2, 3.0, '결과가 일관적이지 않습니다.', '2025-06-15 12:15:00', '2025-06-15 12:15:00'),
                                                                                                                           (27, 3, 3, 4.0, '만족합니다.', '2025-06-15 14:30:00', '2025-06-15 14:30:00'),
                                                                                                                           (28, 36, 1, 2.0, '별로 추천하고 싶지 않네요.', '2025-06-16 09:50:00', '2025-06-16 09:50:00'),
                                                                                                                           (29, 12, 2, 4.5, '창의적인 결과물이 나옵니다.', '2025-06-16 11:35:00', '2025-06-16 11:35:00'),
                                                                                                                           (30, 26, 3, 5.0, '이 프롬프트 덕분에 프로젝트를 끝냈어요!', '2025-06-16 17:00:00', '2025-06-16 17:00:00');