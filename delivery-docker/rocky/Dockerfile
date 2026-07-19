FROM rockylinux:8

# 1. 카프카 및 스칼라 버전 환경 변수 선언
ENV KAFKA_VERSION=3.9.0
ENV SCALA_VERSION=2.13
ENV KAFKA_HOME=/data/kafka

# 2. 패키지 설치
RUN dnf -y update && \
    dnf -y install epel-release && \
    dnf -y install sudo net-tools iproute vim-enhanced nano procps-ng wget curl tar unzip passwd shadow-utils openssh-clients ansible git sshpass openssh-server telnet \
                   java-17-openjdk-headless && \
    dnf clean all

# 3. 계정 세팅 및 시스템 전체 ll 별명 추가
RUN echo "root:fnxm12!@" | chpasswd && \
    useradd -m dev && \
    echo "dev:roqkf12!@" | chpasswd && \
    echo "dev ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers && \
    echo "alias ll='ls -l --color=auto'" >> /etc/bashrc

# 4. 카프카 다운로드 및 설치 (★ mkdir -p /data 추가)
RUN mkdir -p /data && \
    wget https://archive.apache.org/dist/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz && \
    tar -xzf kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /data && \
    ln -s /data/kafka_${SCALA_VERSION}-${KAFKA_VERSION} ${KAFKA_HOME} && \
    rm kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz

WORKDIR $KAFKA_HOME

# 5. SSH 호스트 키 생성 및 필요 디렉터리 생성
RUN ssh-keygen -A && \
    mkdir -p /run/sshd

CMD ["/usr/sbin/sshd", "-D"]