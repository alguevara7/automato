- install lein (https://github.com/technomancy/leiningen)
- install git
- type: git clone git@github.com:Intelliware/automato.git
- create a file named .automato in your home folder
- add the line *user={user}* to .automato, where {user} is the username of the unanet database account 
- add the line *password={password}* to .automato, where {password} is the password of the unanet database account
- add the line *hostname={hostname}* to .automato, where {hostname} is the hostname of the unanet database 
- type: cd automato
- type: lein run {project-code}, where {project-code} ios the project code of the unanet project you would like the generate a report for