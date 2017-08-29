## Database Setup

### Docker flavor

Run a Postgres 9.6 instance :

> docker run -d -p5543:5432 --name test-hib-db -e "POSTGRES_USER=test" -e "POSTGRES_PASSWORD=test" -e "POSTGRES_DB=test"  postgres:9.6 

Check init :

> docker logs test-hib-db
