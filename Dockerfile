FROM jokeswar/base-ctl

RUN apt-get update -yqq && apt-get install -yqq bc
RUN apt-get update -yqq && apt-get install -yqq wget
RUN wget --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.deb
RUN apt-get -qqy install ./jdk-17_linux-x64_bin.deb
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-17-oracle-x64/bin/javac 1919
RUN update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk-17-oracle-x64/bin/javac 1919

COPY ./checker ${CHECKER_DATA_DIRECTORY}
