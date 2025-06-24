FROM jenkins/jenkins:lts
USER root
RUN apt-get update && apt-get install -y git docker.io ansible curl
RUN usermod -aG docker jenkins
RUN git config --global --add safe.directory '*'
RUN curl -LO https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl \
    && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl \
    && rm kubectl
USER jenkins
