# Storywave
CREATE TABLE users (
id VARCHAR(255) PRIMARY KEY,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
nickname VARCHAR(255),
email VARCHAR(255),
role VARCHAR(255),
active_status VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE board (
post_type_id BIGINT PRIMARY KEY,
view_post INT
);

CREATE TABLE access (
user_id VARCHAR(255),
read_content BOOLEAN,
write_content BOOLEAN,
update_content BOOLEAN,
delete_content BOOLEAN,
PRIMARY KEY (user_id),
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE category (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
post_type_id BIGINT,
name VARCHAR(255),
FOREIGN KEY (post_type_id) REFERENCES board(post_type_id)
);

CREATE TABLE posts (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
post_type_id BIGINT,
title VARCHAR(255),
content TEXT,
user_id VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
thumbs INT,
FOREIGN KEY (post_type_id) REFERENCES board(post_type_id),
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
post_id BIGINT,
user_id VARCHAR(255),
content TEXT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (post_id) REFERENCES posts(id),
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE images (
id INT AUTO_INCREMENT PRIMARY KEY,
url VARCHAR(255),
post_id BIGINT,
uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE post_category (
post_id BIGINT,
category_id BIGINT,
PRIMARY KEY (post_id, category_id),
FOREIGN KEY (post_id) REFERENCES posts(id),
FOREIGN KEY (category_id) REFERENCES category(id)
);

테이블을 위와 같이 만들어서 진행함
다른점: 
category랑 post와의 관계 테이블 post_category를 추가함
board가 id가 필요없을 것 같고 숫자가 두개랑 헷갈려서 id를 뺐음
post_type_id가 일단 숫자로 되어 있어서 0이 공지사항 1이 영화 2가 book으로 생각하고 설계
나중에 문자로 변경해도 된다고 생각