# Export Trello Boards 
* Export to Markdown files and attachments to file system
* Save data to json files too
* Only Free Plan features export 


## Get API key and token
https://trello.com/app-key

# Functions
* Boards
* Board
  * Lists
* List
  * Cards
  * Card badge TODO
    * Due date
    * Description
    * Comments
    * Attachments
    * Checklist items
* Card
  * Name
  * Last activity
  * Due date
  * Cover TODO
  * Description
    * Main content 
    * emoji TODO
    * inline attachment link TODO
  * Checklists
  * Attachments TODO
  * Labels TODO
  * Dates? TODO?
  * Comments TODO
  * Activities? TODO?
  * Assignee? TODO?

# TODO
* check api key and token
* time zones 

## Used libs
* https://spring.io/projects/spring-boot
* https://github.com/Steppschuh/Java-Markdown-Generator
* https://square.github.io/okhttp/
* https://github.com/vsch/flexmark-java


## Trello REST API
https://developer.atlassian.com/cloud/trello/rest/


https://developer.atlassian.com/cloud/trello/rest/api-group-actions/#api-gro

curl -H "Authorization: OAuth oauth_consumer_key=\"{{key}}\", oauth_token=\"{{token}}\"" https://api.trello.com/1/cards/5e839f3696a55979a932b3ad/attachments/5edfd184387b678655b58348/download/my_image.png

