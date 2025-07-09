
# AI-Powered Prompt Trading Platform
**💡 2025 KDT-삼육대학교 2차프로젝트로 진행한 “Prtmpt2.0” 의 BE코드입니다.**

**국내외 SNS, 블로그와 같은곳에서 본인만의 프롬프트를 공유하고 거래하는 AI도구 사용자들의 단일화  라는 아이디어로 프로젝트를 진행했습니다.**
<div align=center>
---

![Static Badge](https://img.shields.io/badge/JAVA-17-red?style=for-the-badge&logo=java&logoColor=white&logoSize=auto) ![Static Badge](https://img.shields.io/badge/Spring%20Boot-3.4.5-%236DB33F?style=for-the-badge&logo=spring%20boot&logoColor=white&logoSize=auto) ![Static Badge](https://img.shields.io/badge/MySQL-8.2.0-%234479A1?style=for-the-badge&logo=mysql&logoColor=white&logoSize=auto)

![Static Badge](https://img.shields.io/badge/AWS-S3-green?style=for-the-badge&logoColor=white&logoSize=auto) ![Static Badge](https://img.shields.io/badge/AWS-ECS-%23F96702?style=for-the-badge&logoColor=white&logoSize=auto)
![Static Badge](https://img.shields.io/badge/Auth0-1.5.3-%23EB5424?style=for-the-badge&logo=auth0&logoColor=white&logoSize=auto)

![Static Badge](https://img.shields.io/badge/Docker-28.2-%232496ED?style=for-the-badge&logo=docker&logoColor=white&logoSize=auto)
</div>

**“AI 평가 기반 프롬프트 거래 플랫폼”**

여러곳에 흩어져 있는 프롬프트 공유/판매자 → AI의 평가 통한 프롬프트 검증 → 거래 수단의 단일화


## 🧠 프로젝트 기획

> **“AI평가로 높은 신뢰성의 프롬프트 거래”**
SNS, 블로그 등 프롬프트 거래 플랫폼의 부재로 개인간의 거래가 이루어지는 **AI도구 사용자들을 단일화**,
**AI의 프롬프트 평가**와 **판매자 이력평가**로 투명한 거래와 함께 판매자의 PR까지 가능한 **사용자 중심 플랫폼**
> 

## 🛠️ 핵심 기능

- **🤖 AI의 프롬프트 평가** : 판매하기 위한 프롬프트를 등록과 동시에 AI의 평가 진행
- ⭐️ **높은 등급 추천** : 높은 등급을 받은 프롬프트에 대해 “**AI Excellent**” 뱃지 부여
- 📝 **맞춤형 AI평가** : 텍스트, 이미지, 영상 생성형 프롬프트별 평가 기준 구분

---

## 🏗️ 시스템 아키텍처

![컴포넌트다이어그램](https://github.com/AI-Prompt-Trade-Platform/BE/blob/master/Prumpt%E1%84%8F%E1%85%A5%E1%86%B7%E1%84%91%E1%85%A9%E1%84%82%E1%85%A5%E1%86%AB%E1%84%90%E1%85%B3%E1%84%83%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%A5%E1%84%80%E1%85%B3%E1%84%85%E1%85%A2%E1%86%B7.pdf)


## 🎯 기능 Overview

### 🔐 인증&보안

- **Auth0 소셜 로그인** (Google, Github)
- **개발/운영 환경변수 분리적용**(.env 파일관리)
- **JWT기반 사용자 인증/세션유지**
- **결과예시 데이터와 프롬프트 본문의 분리저장**
    - 정적데이터 : **AWS CloudFront** ← AOS정책 → **S3**
    - 프롬프트 본문 : **AWS RDS + Security Group**

### 🤖 AI평가 시스템

- **OpenAI API** (gpt-4o)
- **S3 Presigned URL** 제공 → AI의 S3 정적데이터 일시적 접근, 평가

### 📝 프롬프트 등록/거래

- **MultipartFile 이용한 프롬프트 결과예시 데이터 업로드**
- **AWS S3 버킷에 업로드/삭제 자동화**

