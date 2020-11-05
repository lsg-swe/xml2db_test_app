# XML to DB Test Application 1.0.0
Parses input XML file and stores data to the configured database.
XML file element example
```xml
  <transaction>
    <place>A PLACE 1</place>
    <amount>10.01</amount>
    <currency>UAH</currency>
    <card>123456****1234</card>
    <client>
        <firstName>Ivan</firstName>
        <lastName>Ivanoff</lastName>
        <middleName>Ivanoff</middleName>
        <inn>1234567890</inn>
    </client>
  </transaction>
```
## Implementation Overview
Data entities for persistence: Client, Place, BankingTransaction. 
Input files can be huge therefore they are processed as a stream.
Uses Spring Data JPA (database configuration _/resources/application.properties_). 

Single BankingTransaction object is processed as a single db transaction. 
If the CLient in the banking transaction is found in the DB (search by inn), its id (PK) is used. Otherwise new Client is added to the DB.
If the Place in the banking transaction is found in the DB (search by place name), its id (PK) is used. Otherwise new Place is added to the DB.
If one BankingTransaction entry processing fails, rollback is issued for this entry + related Client and Place updates.

Application by default is configured to use PostgreSQL database (_/resources/application.properties_). 
Unit tests are configured to use in-memory H2 database (_/resources/application-test.properties_).

Maven is used to build application.

## Configuration
Application configuration file _/resources/application.properties_
Database type
  _spring.jpa.database=POSTGRESQL_
Dump JPA SQL queries to standard out 
  _spring.jpa.show-sql=true_

Initialize database (the standard Hibernate property values, can be set to ''none'', ''validate'', ''update'', ''create-drop'') 
  _spring.jpa.hibernate.ddl-auto=create_

__Note__
In the case of PostgeSQL, database itself and user with granted access shall exist before application run. Test db can be created as 
_CREATE DATABASE <db_name>;
CREATE USER <user_name> WITH PASSWORD <user_pwd>;
GRANT ALL PRIVILEGES ON DATABASE <db_name> TO <user_name>;_

_JPA logging_ 
_logging.level.org.hibernate.SQL=ERROR_
_Log transaction details_
_logging.level.org.springframework.orm.jpa=ERROR
logging.level.org.springframework.transaction=ERROR_

Test configuration file _/resources/application-test.properties_
_Unit tests are configured to use in-memory h2 database_

Dump JPA SQL queries to standard out. 
_hibernate.show_sql=true_
Initialize database (the standard Hibernate property values, can be set to ''none'', ''validate'', ''update'', ''create-drop'') 
_hibernate.hbm2ddl.auto=create-drop_

## Usage

* build and run tests
  $ mvn clean package

* run
  $ mvn spring-boot:run -Dspring-boot.run.arguments=<file.name>
    or
  $ java -jar xml2db-0.0.1-SNAPSHOT.jar <file.name>

## [License](LICENSE)
Copyright (c) 2020 larysa <lsg_swe@hotmail.com> 

All code found in this repository is licensed under GPL v3
You should have received a copy of the GNU General Public License
along with XML2DB application. If not, see <http://www.gnu.org/licenses/>.


