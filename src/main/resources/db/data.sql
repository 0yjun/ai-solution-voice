INSERT INTO terms (id, type, title, content, version, is_required, created_at, updated_at)
VALUES
    (1, 'PRIVACY', '개인정보 처리방침', '개인정보에 대한 내용입니다.', 'v1.0', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'TERMS', '이용약관', '서비스 이용약관 내용입니다.', 'v1.0', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 현재 적용된 약관 매핑
INSERT INTO applied_terms (id, type, terms_id, applied_at)
VALUES
    (1, 'PRIVACY', 1, CURRENT_TIMESTAMP),
    (2, 'TERMS', 2, CURRENT_TIMESTAMP);