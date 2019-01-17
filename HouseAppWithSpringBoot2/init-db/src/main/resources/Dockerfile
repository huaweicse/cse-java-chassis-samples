from 100.125.0.198:20202/servicestage/mysqlutf8:0.2
COPY ./init-db/target/sql /sql
COPY ./init-db/target/entrypoint.sh /
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
CMD ["--help"]
