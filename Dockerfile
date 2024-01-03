FROM postgres:latest

ARG DB_USERNAME
ARG DB_PASSWORD

ENV POSTGRES_USER=${DB_USERNAME}
ENV POSTGRES_PASSWORD=${DB_PASSWORD}
ENV POSTGRES_DB=shortener

#docker build --build-arg DB_USERNAME=admin --build-arg DB_PASSWORD=admin -t db_postgres .
#docker run --name postgresql -d -p 5432:5432 db_postgres

#docker start container postgresql
#docker stop container postgresql