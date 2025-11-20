# 📝 Enrollment Module

# 📝 Enrollment Module

🧩 1. 수강 등록 (결제 완료 시 자동 등록)
🔸 시나리오

사용자가 강좌 상세 페이지에서 “결제하기” 클릭

결제 성공 → Enrollment 테이블에 자동으로 한 줄 생성

상태(Status)는 기본 ACTIVE

🧠 핵심 개념
주제	설명
Spring MVC 흐름	Controller → Service → Repository 구조 이해
JPA 기본 CRUD	save(), findById() 등 사용법
엔티티 관계 매핑	@ManyToOne(User, Course) 등 관계 설정
트랜잭션 처리	결제 성공 후 수강 등록이 동시에 일어나야 하는 이유 이해 // TODO : 트랜잭션에 관한 영상 혹은 글 확인
DTO (Data Transfer Object)	요청/응답을 위한 데이터 구조 설계

💳 2. 환불 시 수강 취소 (삭제 또는 상태 변경)
🔸 시나리오

사용자가 결제 환불을 요청하면, 해당 강좌 수강 기록을 지워야 함.

🔸 구현 방식 두 가지

🧠 핵심 개념
주제	설명
DELETE / PATCH 요청	REST API에서 데이터 변경 방식 ->
JPA delete()와 soft delete	실제 삭제 vs 상태 변경 차이
Enum 상태 관리	Status Enum 활용 (ACTIVE → CANCELED)

🧾 3. 마이페이지 - 나의 수강 목록 보기
🔸 시나리오

로그인한 사용자가 “내 수강목록” 클릭 시

숨기지 않은(hidden=false) 수강 내역만 보여줌

🧠 핵심 개념
주제	설명
로그인 사용자 정보 가져오기	@AuthenticationPrincipal 또는 JWT에서 userId 추출
JPA 쿼리 메서드	findByUserAndHiddenFalse() 같은 맞춤형 쿼리 작성
DTO 변환	엔티티 → 응답용 DTO로 변환해서 JSON 반환

👁️ 4. 수강 목록에서 “숨기기” 버튼
🔸 시나리오

사용자가 특정 수강 강좌를 “목록에서 숨기기” 클릭

실제로는 삭제가 아니라, hidden = true 로 업데이트

🧠 핵심 개념
주제	설명
PATCH 요청 처리	리소스 일부만 수정할 때 PATCH 사용
JPA 엔티티 업데이트	entity.setHidden(true) → save() 처리
엔티티 상태 관리	영속성 컨텍스트의 작동 원리 이해

👻 5. 숨긴 강좌 목록 + 복구 기능
🔸 시나리오

“숨긴 강좌 보기” 페이지에서
→ hidden = true 인 수강 내역만 조회

“복구하기” 버튼 클릭 시
→ hidden = false 로 변경

🧠 핵심 개념
주제	설명
조건 조회 쿼리 작성	findByUserAndHiddenTrue()
상태 토글 로직	true → false, false → true
Controller에서 경로 변수와 요청 처리	@PathVariable Long id

🔄 6. 강좌 목록 순서 변경
🔸 시나리오

사용자가 마이페이지에서 “수강목록 순서 변경” 기능을 사용
(드래그앤드롭, 순위 버튼 등)

변경된 순서가 DB에 반영되어야 함

🧠 핵심 개념
주제	설명
배열 형태 요청 처리	프론트에서 순서 데이터 배열로 보내기 (List<OrderRequest>)
배치 업데이트	여러 엔티티를 한 번에 수정하기
트랜잭션 처리	순서 변경 중 오류 시 전체 롤백
정렬 쿼리 작성	findByUserOrderByOrderIndexAsc()
프론트 연동 이해	순서 변경 시 어떻게 데이터를 보내는지 구조 파악
