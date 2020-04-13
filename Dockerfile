FROM clojure:lein-2.9.1

RUN apt-get update
RUN apt-get install -y httpie
