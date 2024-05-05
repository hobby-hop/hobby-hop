<img src="https://capsule-render.vercel.app/api?type=waving&color=768CFF&height=160&text=HOBBY%20HOP!🐇&fontSize=100&fontColor=000000&section=footer&animation=fadeIn" />



# 취미 공유 플랫폼 **HOBBYHOP**
![77a22d572ed54c9caab83312ed73ab21azh4ml5qoozsnxst-0](https://github.com/hobby-hop/hobby-hop/assets/148298032/5d496bfe-c8a9-461a-a336-5632a14b62fe)

Hobby Hop은 사용자들이 자신의 취미를 등록하고 다양한 형식으로 공유하며, 취미에 관심 있는 다른 사용자들과 소통할 수 있는 플랫폼입니다.


---


## 🐇 프로젝트 소개
- Hobby Hop을 통해 사용자들은 자신만의 독특한 방식으로 취미를 공유하고 표현할 수 있습니다.
- Hobby Hob은 공통된 관심사를 가진 사람들이 모여 커뮤니티를 형성하여 서로 소통하고 경험을 나눌 수 있는 기회를 제공합니다.

1. 나만의 취미 모임을 만들거나 다른 유저가 만든 모임에 가입할 수 있습니다.
2. 게시물을 통해 같은 관심사를 가진 유저와 경험, 팁을 공유하고 댓글을 통해 활발한 소통에 참여할 수 있습니다.

### 🐇 [노션](https://teamsparta.notion.site/Hobby-Hop-75087879d9d94c0c9627d40e236ba049)
### 🐇 [사이트 링크](https://hobbyhop.site/login.html)



---


## 🐇 프로젝트 목표
- 깃 플로우 따르기
- 팀원간 적절한 논의를 통해 기능 구현보다 성능 개선에 주안점을 둠.
- 협업을 위한 깃 컨벤션과 코드 컨벤션을 정하고, 코드의 재사용성 및 확장성을 고려하여 구현하는 것을 목표로 개발


---


## 🐇 개발 기간 
- 2024.01.04(목) ~ 2024.02.08(목)
- S.A. 작성
- 개발 시작
- 중간 발표
- 유저 테스트
- 유저 피드백 반영
- 최종 발표


---

  
## 🐇 팀 소개 및 역할 분담
- **권준혁** : 리더   - 그룹, 배포, 프론트
- **홍효정** : 부리더 - 댓글, 코드 퀄리티 개선 및 컨벤션 점검
- **김한준** : 팀원   - 게시글, 배포, 성능테스트, CI/CD
- **송지헌** : 팀원   - 와이어 프레임, 대댓글, 프론트 구조 제작
- **정유진** : 팀원   - ERD 설계, 유저, 시큐리티, 배포

이름 | 역할 | Github | 블로그 
--|--|--|--
권준혁 | 팀장 | [https://github.com/jh10253267](https://github.com/jh10253267) | [https://github.com/jh10253267/TIL](https://github.com/jh10253267/TIL)
홍효정 | 부팀장 | [https://github.com/dearel4416](https://github.com/dearel4416) | [https://javach1p.tistory.com/](https://javach1p.tistory.com/)
김한준 | 팀원 | [https://github.com/wkdehf217](https://github.com/wkdehf217) | [https://velog.io/@wkdehf217/posts](https://velog.io/@wkdehf217/posts)
송지헌 | 팀원 | 공사중 | [https://github.com/pepsisong](https://github.com/pepsisong)
정유진 | 팀원 | [https://github.com/kkamjjing2](https://github.com/kkamjjing2) | [https://kkamjjing.tistory.com/](https://kkamjjing.tistory.com/)


---


## 🐇 개발환경
- **Version** : Java 17
- **IDE** : IntelliJ
- **Framework** : SpringBoot 3.2.1
- **ORM** : JPA


---


## 🐇 기술 스택
- **Server** : AWS EC2, AWS S3
- **DataBase** : AWS RDS, H2, JPQL, MySQL, Redis 7.2.4
- **Container distribution** : Docker
- **아이디어 회의** : Slack, Zep, Notion


---


## 🐇 프로젝트 아키텍처
![image](https://github.com/hobby-hop/hobby-hop/assets/108499717/84a8e0fc-4757-4b27-884a-28f3915122fe)


---


## 🐇 주요 기능
- 모임
- 게시판

---


## 🐇 ERD
![image](https://github.com/hobby-hop/hobby-hop/assets/148298032/0ab02635-06e5-4f11-8d08-eea68875a1ba)


---


## 🐇 기술적 의사결정

[Main DB 결정](https://github.com/hobby-hop/hobby-hop/wiki/AWS-Mysql-RDS-VS-%EB%B3%84%EB%8F%84%EC%9D%98-Mysql-EC2-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4)  
[Github Actions VS Jenkins](https://github.com/hobby-hop/hobby-hop/wiki/AWS-Mysql-RDS-VS-%EB%B3%84%EB%8F%84%EC%9D%98-Mysql-EC2-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4)  
[복합키 사용](https://github.com/hobby-hop/hobby-hop/wiki/%EB%B3%B5%ED%95%A9%ED%82%A4-%EC%82%AC%EC%9A%A9)    
[QueryDSL VS JPQL](https://github.com/hobby-hop/hobby-hop/wiki/QueryDSL-VS-JPQL)


---

## 🐇 성과

[CI 시간 단축](https://github.com/hobby-hop/hobby-hop/wiki/CI-%EC%8B%9C%EA%B0%84-%EB%8B%A8%EC%B6%95)

---

## 🐇 API 명세서
![스크린샷 2024-01-29 201945](https://github.com/hobby-hop/hobby-hop/assets/148298032/83c34e7e-247d-4200-bd4a-4a76717aa09c)
![스크린샷 2024-01-29 202027](https://github.com/hobby-hop/hobby-hop/assets/148298032/eae7ee75-2b80-4ce3-b9ec-555ea85d37fa)
![스크린샷 2024-01-29 202051](https://github.com/hobby-hop/hobby-hop/assets/148298032/be669b80-4e04-49b4-a051-0c94c35f2d82)
![스크린샷 2024-01-29 202109](https://github.com/hobby-hop/hobby-hop/assets/148298032/e81a4257-9db5-40be-8a99-7b9f6a779920)
![스크린샷 2024-01-29 202129](https://github.com/hobby-hop/hobby-hop/assets/148298032/1e22e440-3b57-4b12-85a5-649b2c45c72b)


