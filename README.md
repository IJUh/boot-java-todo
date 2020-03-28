# boot-java-todo
Spring Bood Java Project (Todo app)

## 개발환경
| Name | Version | Description |
| --- | --- | --- |
| Java | `1.8` |
| Gradle | `6.2.2` |
| Spring Boot | `2.2.5.RELEASE` |
| Lombok | 
| H2Database |

## H2 Database console URL
http://localhost:8080/h2-console
- url : jdbc:h2:mem:test
- name : sa

## Todo 웹 페이지
http://localhost:8080/index.html

## HTTP Status Code
성공
- 200 OK (성공)
- 204 No Content (콘텐츠 없음) - 데이터가 없을 경우

실패
- 400 Bad Request (잘못된 요청) - 데이터 검증 실패, 데이터 포맷이 안맞거나 필수값 누락 등의 검증 실패
- 403 Forbidden (금지됨) - todo 항목 참조시, 참조할 수 없는 항목인 경우
- 412 Precondition Failed (사전 조건 실패) - 요청 서비스에 대한 사전 조건이 완료되지 않은 경우(완료상태 변경시 참조 항목이 미완료우 상태인 경)

