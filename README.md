# 🌖 달무리 (Dal-muri)

> 개발자의 일일 학습 기록(TIL: Today I Learned)을 관리하고, AI가 월간 회고를 생성해 주는 안드로이드 앱입니다.

## 📌 주요 기능
- **TIL 작성 및 관리**: 매일 배운 내용을 기록하고 Room Database를 통해 로컬 환경에서 안전하게 관리합니다.
- **학습 대시보드 (Chart)**: 주간/월간 학습 통계를 차트로 시각화하여 직관적으로 확인할 수 있습니다.
- **AI 월간 회고 생성 (Review)**: 사용자가 기록한 한 달 치의 TIL 데이터를 기반으로 OpenAI API를 활용하여 맞춤형 월간 회고 피드백을 자동 생성해 줍니다.
- **로딩 피드백**: AI 분석 등 일정 시간이 소요되는 작업 진행 시, Material 3의 로딩 인디케이터 기반으로 피드백을 제공합니다.

## 🏛 아키텍처 및 디자인 패턴
- **Layered Clean Architecture**: `data` (인프라 및 레포지토리 구현현), `domain` (비즈니스 로직, 모델 및 유스케이스 구현), `presentation` (UI 및 상태 관리) 계층의 명확한 역할 분리
- **MVI (Model-View-Intent)**: [Orbit MVI 라이브러리](https://github.com/orbit-mvi/orbit-mvi) 구조를 통해 단방향 데이터 흐름을 강제하고 뷰 상태(State) 및 사이드 이펙트(Side Effect) 처리를 체계적으로 관리합니다.
- **의존성 주입 (DI)**: Dagger Hilt를 사용하여 보일러플레이트 없는 의존성 주입을 구현하였습니다.

## 🛠 주요 기술 스택 (Tech Stack)

### Language & Toolkit
- **Kotlin** (Target JVM 17)
- **Android SDK** 35 (Min SDK 26)
- **Android Gradle Plugin (AGP)** 8.7.3 (ktlint 정적 분석 호환성을 위한 고정 버전)

### UI & Architecture
- **Jetpack Compose** & **Material 3**
- **Navigation Compose**
- **Orbit MVI** (Core, ViewModel, Compose 액스텐션)
- **Hilt** (Dependency Injection)
- **Vico Compose** (Compose 차트 그리기 라이브러리)

### Data & Network Async
- **Coroutines & Flow**
- **Room Database** (Local Storage)
- **Retrofit2 & OkHttp3** (Network API Calls)
- **Kotlinx Serialization** (JSON Serialization)
