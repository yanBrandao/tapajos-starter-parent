FROM maven:3.6.3-jdk-11

WORKDIR /root/app/

RUN apt-get update && apt-get install -y --no-install-recommends apt-utils \
        &&  apt-get install gnupg2 -y

RUN export GPG_TTY=$(tty)


        ##RUN gpg --keyserver hkp://keyserver.ubuntu.com --recv-keys 8E8CAD05756FCC8F
        # gpg2 --gen-key
    ##RUN gpg2 --keyserver hkp://keyserver.ubuntu.com --send-key 2AE559D7181DA360

COPY . ./tapajos-starter-parent

ENTRYPOINT [ "/bin/bash" ]