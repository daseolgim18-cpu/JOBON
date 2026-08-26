# JOBON

> **취업 준비를 한 곳에서 관리하는 통합 취업 준비 서비스 플랫폼**

JOBON은 관심 기업, 채용공고, 지원 현황, TODO, 성장 기록, 프로젝트 경험 등
취업 준비 과정에서 발생하는 정보를 하나의 서비스에서 통합 관리하기 위한 개인 프로젝트입니다.

단순히 채용공고를 조회하는 서비스가 아니라,
사용자가 직접 축적한 **취업 준비 데이터와 개발 경험을 연결하여 관리하는 것**을 목표로 합니다.

---

## 프로젝트 개요

취업 준비 과정에서는 기업 정보, 채용공고, 지원 일정, 학습 내용, 프로젝트 경험 등이 여러 곳에 분산되기 쉽습니다.

JOBON은 이러한 정보를 한 곳에 모아 관리하고,

**기업 → 채용공고 → 지원 → TODO → 성장 기록 → 프로젝트 경험 → AI 분석**

으로 이어지는 취업 준비 흐름을 하나의 서비스에서 확인할 수 있도록 개발합니다.

또한 저장된 채용공고와 사용자의 프로젝트 경험을 활용하여
채용공고 분석과 자소서 활용 경험 추천 등의 AI 보조 기능을 제공합니다.

---

## 주요 기능

### 1. 회원 및 인증

- 회원가입
- 로그인 / 로그아웃
- 아이디 찾기
- 비밀번호 찾기 및 변경
- 회원 정보 관리
- 프로필 관리
- SNS 계정 연동
- 소셜 로그인

---

### 2. 대시보드

사용자의 취업 준비 현황을 한 화면에서 확인합니다.

- 지원 현황 요약
- 마감 임박 채용공고
- 최근 활동
- TODO 현황
- 지원 상태 통계
- 최근 AI 분석 결과
- 자소서 경험 추천 결과

---

### 3. 기업 관리

관심 있는 기업 정보를 직접 등록하여 관리합니다.

- 기업 등록
- 기업 목록 조회
- 기업 검색
- 기업 상세 조회
- 기업 정보 수정
- 기업 삭제
- 기업별 채용공고 확인
- 기업 메모 관리

---

### 4. 채용공고 관리

여러 채용 사이트에서 확인한 관심 채용공고를 JOBON에 저장하여 관리합니다.

- 채용공고 등록
- 채용공고 목록 조회
- 채용공고 검색
- 직무별 조회
- 정렬
- 채용공고 상세 조회
- 채용공고 수정
- 채용공고 삭제
- 공고 출처 URL 관리
- 마감일 관리
- 채용공고 원문 및 메모 관리

---

### 5. 지원 현황 관리

등록된 채용공고를 기준으로 실제 지원 진행 상황을 관리합니다.

- 지원 내역 조회
- 기업별 검색
- 지원 상태별 조회
- 지원 상태 관리
- 지원 일정 관리
- 지원 관련 메모 관리
- 연결된 채용공고 확인
- 지원 일정 기반 TODO 생성
- 관련 AI 분석 결과 확인

---

### 6. TODO 관리

취업 준비 과정에서 필요한 할 일을 관리합니다.

- TODO 등록
- TODO 조회
- TODO 수정
- TODO 삭제
- 우선순위 설정
- 마감일 설정
- 진행 상태 관리
- 관련 기업 연결
- 관련 채용공고 연결
- 메모 관리

---

### 7. 성장 기록

학습 및 취업 준비 과정에서 쌓은 성장 내용을 기록합니다.

- 학습 기록
- 자격증 기록
- 활동 기록
- 학습 날짜 관리
- 기술 키워드 관리
- 학습 내용 기록
- 어려웠던 점 기록
- 느낀 점 및 활용 계획 기록

---

### 8. 프로젝트 경험

취업 및 자기소개서 작성에 활용할 수 있는 개발 경험을 관리합니다.

- 프로젝트 등록
- 프로젝트 조회
- 프로젝트 수정
- 프로젝트 삭제
- 프로젝트 진행 기간 관리
- 담당 역할 관리
- 사용 기술 관리
- 주요 수행 경험 관리
- 프로젝트 기능 관리
- 트러블슈팅 경험 관리
- 프로젝트 관련 URL 관리

---

### 9. AI 분석

등록한 채용공고와 사용자의 경험 데이터를 기반으로 AI 보조 기능을 제공합니다.

- 채용공고 AI 분석
- 주요 업무 분석
- 자격 요건 분석
- 우대 사항 분석
- 요구 역량 분석
- 요구 기술 분석
- 사용자 경험과 채용공고 연결
- 자소서 활용 프로젝트 경험 TOP3 추천
- AI 분석 결과 저장
- AI 재분석

---

### 10. 마이페이지

사용자 개인 정보와 JOBON 활동 정보를 관리합니다.

- 내 프로필 조회
- 프로필 수정
- 비밀번호 변경
- SNS 연동 계정 관리
- 활동 내역 조회
- 저장된 검색 조건 관리

---

## 서비스 흐름

```text
회원가입 / 로그인
        │
        ▼
   관심 기업 등록
        │
        ▼
   채용공고 등록
        │
        ▼
   지원 현황 관리
        │
        ├───────────┐
        ▼           ▼
    TODO 관리    AI 분석
        │           │
        ▼           │
    성장 기록       │
        │           │
        ▼           │
 프로젝트 경험 관리 ─┘
        │
        ▼
  경험 TOP3 추천
        │
        ▼
 대시보드 통합 확인
```

---

## 기술 스택

### Backend

- Java 21
- Spring Boot
- Spring MVC
- MyBatis

### Frontend

- JSP
- HTML5
- CSS3
- JavaScript

### Database

- Oracle Database 21c

### Build / Server

- Maven
- Apache Tomcat
- WAR Packaging

### Version Control

- Git
- GitHub

---

## Backend Architecture

JOBON은 다음과 같은 계층형 구조를 사용합니다.

```text
Controller
    ↓
Service
    ↓
ServiceImpl
    ↓
DAO
    ↓
MyBatis Mapper XML
    ↓
Oracle Database
```

기능별 패키지를 분리하여 각 도메인의 Controller, Service, DAO, DTO, VO를 관리합니다.

---

## 프로젝트 구조

```text
jobon/
│
├── .mvn/
│
├── src/
│   │
│   ├── main/
│   │   │
│   │   ├── java/com/jobon/
│   │   │   │
│   │   │   ├── JobonApplication.java
│   │   │   ├── ServletInitializer.java
│   │   │   │
│   │   │   ├── common/
│   │   │   │   ├── config/
│   │   │   │   ├── exception/
│   │   │   │   └── util/
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── MainController.java
│   │   │   │
│   │   │   ├── member/
│   │   │   ├── social/
│   │   │   ├── dashboard/
│   │   │   ├── company/
│   │   │   ├── job/
│   │   │   ├── apply/
│   │   │   ├── todo/
│   │   │   ├── learning/
│   │   │   ├── project/
│   │   │   ├── ai/
│   │   │   ├── activity/
│   │   │   └── savedsearch/
│   │   │
│   │   ├── resources/
│   │   │   │
│   │   │   ├── application.properties
│   │   │   │
│   │   │   ├── mapper/
│   │   │   │   ├── member/
│   │   │   │   ├── social/
│   │   │   │   ├── dashboard/
│   │   │   │   ├── company/
│   │   │   │   ├── job/
│   │   │   │   ├── apply/
│   │   │   │   ├── todo/
│   │   │   │   ├── learning/
│   │   │   │   ├── project/
│   │   │   │   ├── ai/
│   │   │   │   ├── activity/
│   │   │   │   └── savedsearch/
│   │   │   │
│   │   │   └── static/
│   │   │       ├── css/
│   │   │       ├── js/
│   │   │       └── images/
│   │   │
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               ├── common/
│   │               ├── member/
│   │               ├── dashboard/
│   │               ├── company/
│   │               ├── job/
│   │               ├── apply/
│   │               ├── todo/
│   │               ├── learning/
│   │               ├── project/
│   │               ├── ai/
│   │               ├── mypage/
│   │               └── main.jsp
│   │
│   └── test/
│       └── java/com/jobon/
│           └── JobonApplicationTests.java
│
├── .gitattributes
├── .gitignore
├── README.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```

> `application.properties`는 DB 접속 정보 및 외부 API 관련 설정 보호를 위해 Git 관리 대상에서 제외합니다.

---

## Database

JOBON의 주요 테이블은 다음과 같습니다.

| 테이블                    | 역할                |
| ------------------------- | ------------------- |
| `JOBON_MEMBER`            | 회원 및 프로필 정보 |
| `SOCIAL_ACCOUNT`          | SNS 연동 계정       |
| `COMPANY`                 | 관심 기업 정보      |
| `JOB_POSTING`             | 채용공고            |
| `APPLICATION`             | 지원 현황           |
| `TODO_ITEM`               | TODO                |
| `LEARNING_RECORD`         | 성장 기록           |
| `LEARNING_TECH`           | 성장 기록-기술 연결 |
| `TECH_STACK`              | 기술 스택           |
| `PROJECT_EXPERIENCE`      | 프로젝트 경험       |
| `PROJECT_TECH`            | 프로젝트-기술 연결  |
| `PROJECT_FEATURE`         | 프로젝트 주요 기능  |
| `PROJECT_TROUBLE`         | 프로젝트 트러블슈팅 |
| `AI_ANALYSIS`             | 채용공고 AI 분석    |
| `AI_JOB_TECH`             | AI 분석 기술 정보   |
| `AI_EXPERIENCE_RECOMMEND` | 프로젝트 경험 추천  |
| `ACTIVITY_LOG`            | 사용자 활동 기록    |
| `SAVED_SEARCH`            | 저장된 검색 조건    |

### 주요 데이터 관계

```text
JOBON_MEMBER
    │
    ├── COMPANY
    │      └── JOB_POSTING
    │              │
    │              ├── APPLICATION
    │              ├── TODO_ITEM
    │              └── AI_ANALYSIS
    │
    ├── LEARNING_RECORD
    │      └── LEARNING_TECH
    │              └── TECH_STACK
    │
    ├── PROJECT_EXPERIENCE
    │      ├── PROJECT_TECH
    │      │       └── TECH_STACK
    │      ├── PROJECT_FEATURE
    │      └── PROJECT_TROUBLE
    │
    ├── ACTIVITY_LOG
    ├── SAVED_SEARCH
    └── SOCIAL_ACCOUNT
```

---

## 화면 구성

```text
메인
│
├── 회원
│   ├── 로그인
│   ├── 회원가입
│   ├── 아이디 찾기
│   └── 비밀번호 찾기
│
├── 대시보드
│
├── 기업 관리
│   ├── 목록
│   ├── 등록
│   ├── 상세
│   └── 수정
│
├── 채용공고
│   ├── 목록
│   ├── 등록
│   ├── 상세
│   └── 수정
│
├── 지원 현황
│   ├── 목록
│   ├── 상세
│   └── 수정
│
├── TODO
│   ├── 목록
│   ├── 등록
│   └── 수정
│
├── 성장 기록
│   ├── 목록
│   ├── 등록
│   ├── 상세
│   └── 수정
│
├── 프로젝트 경험
│   ├── 목록
│   ├── 등록
│   ├── 상세
│   └── 수정
│
├── AI 분석
│   ├── 분석 목록
│   ├── 채용공고 분석
│   ├── 분석 상세
│   └── 경험 TOP3 추천
│
└── 마이페이지
    ├── 프로필
    ├── 비밀번호 변경
    ├── 연동 계정
    ├── 활동 내역
    └── 저장된 검색 조건
```

---

## Git Branch Strategy

JOBON은 `main`, `develop`, `feature` 브랜치를 기준으로 개발합니다.

```text
main
 │
 └── develop
       │
       ├── feature/main
       ├── feature/member
       ├── feature/social
       ├── feature/dashboard
       ├── feature/company
       ├── feature/job
       ├── feature/apply
       ├── feature/todo
       ├── feature/learning
       ├── feature/project
       ├── feature/ai
       ├── feature/mypage
       │
       ├── fix/*
       └── docs/*
```

### `main`

최종 테스트가 완료된 배포 가능한 코드를 관리합니다.

### `develop`

각 기능 브랜치에서 완료된 기능을 통합하고 테스트합니다.

### `feature/*`

각 기능을 독립적으로 개발합니다.

예:

```text
feature/member
feature/company
feature/job
feature/todo
feature/ai
```

### `fix/*`

개발 과정에서 발생한 오류를 수정합니다.

### `docs/*`

README 등 프로젝트 문서를 관리합니다.

---

## Commit Convention

| Type       | 설명                            |
| ---------- | ------------------------------- |
| `feat`     | 새로운 기능 추가                |
| `fix`      | 오류 수정                       |
| `refactor` | 기능 변경 없는 코드 구조 개선   |
| `style`    | CSS 및 UI 수정                  |
| `docs`     | 문서 작성 및 수정               |
| `test`     | 테스트 코드 작성 및 수정        |
| `chore`    | 프로젝트 설정 및 빌드 관련 작업 |

### Example

```text
chore: initialize JOBON project

feat: 회원 로그인 기능 구현

feat: 기업 등록 기능 구현

feat: 채용공고 CRUD 구현

feat: 지원 상태 변경 기능 구현

feat: TODO 관리 기능 구현

feat: 성장 기록 관리 기능 구현

feat: 프로젝트 경험 관리 기능 구현

feat: AI 채용공고 분석 기능 구현

fix: 로그인 세션 오류 수정

style: 메인 페이지 UI 수정

docs: README 수정
```

---

## Git Workflow

기능 개발은 `develop` 브랜치에서 새로운 `feature` 브랜치를 생성하여 진행합니다.

```bash
git checkout develop
git pull origin develop
git checkout -b feature/member
```

기능 개발 후:

```bash
git add .
git commit -m "feat: 회원 로그인 기능 구현"
git push -u origin feature/member
```

기능 확인 후 `develop` 브랜치에 병합합니다.

```bash
git checkout develop
git merge feature/member
git push origin develop
```

전체 기능 개발과 테스트가 완료되면 `main`에 병합합니다.

```bash
git checkout main
git merge develop
git push origin main
```

---

## 실행 방법

### 1. Repository Clone

```bash
git clone https://github.com/daseolgim18-cpu/JOBON.git
```

```bash
cd JOBON
```

### 2. 환경 설정

`application.properties`는 GitHub Repository에 포함되지 않습니다.

Oracle DB 접속 정보 및 필요한 외부 API 설정을 로컬 환경에 별도로 구성해야 합니다.

### 3. Maven Build

Windows:

```bash
mvnw.cmd clean package
```

Maven이 설치되어 있는 경우:

```bash
mvn clean package
```

### 4. 실행

Windows:

```bash
mvnw.cmd spring-boot:run
```

또는:

```bash
mvn spring-boot:run
```

---

## 개발 진행 상태

현재 JOBON은 설계 및 기본 프로젝트 환경 구성을 완료하고 기능별 구현을 진행하고 있습니다.

- [x] 프로젝트 주제 선정
- [x] 요구사항 정의
- [x] 메뉴 구조 설계
- [x] 스토리보드 작성
- [x] ERD 설계
- [x] API 명세 설계
- [x] Spring Boot 프로젝트 생성
- [x] Git / GitHub Repository 구성
- [ ] 회원 및 인증 구현
- [ ] 기업 관리 구현
- [ ] 채용공고 관리 구현
- [ ] 지원 현황 구현
- [ ] TODO 구현
- [ ] 성장 기록 구현
- [ ] 프로젝트 경험 구현
- [ ] 대시보드 구현
- [ ] AI 분석 및 경험 추천 구현
- [ ] 전체 통합 테스트
- [ ] 배포

---

## 프로젝트 목표

JOBON은 기존 채용 사이트의 기능을 단순히 구현하는 것이 아니라,
여러 채용 서비스와 개인 기록에 흩어져 있는 취업 준비 데이터를 하나의 흐름으로 연결하는 것을 목표로 합니다.

사용자가 직접 저장한 **기업, 채용공고, 지원 기록, TODO, 학습 기록, 프로젝트 경험**을 중심으로 데이터를 축적하고,

이를 기반으로 자신의 취업 준비 현황을 파악하고
채용공고의 요구사항과 실제 개발 경험을 연결할 수 있도록 구현합니다.

---

## Developer

**김다설**

개인 프로젝트 · JOBON
