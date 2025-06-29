# Export Trello Boards 
* Export all Boards, Lists, Cards and attachments as Markdown files to local file system
* Save data to json files too
* Export only Free Plan features data 

## Configure
* get API key and token: https://trello.com/app-key
* set TRELLO_API_KEY environment variable 
* set TRELLO_TOKEN environment variable

## Functions
* Boards
* Board
* List
* Card badge
  * Due date
  * Description
  * Comments
  * Attachments
  * Checklist items
* Card
  * Name
  * List name
  * Last activity
  * Due date
  * Cover TODO
  * Description
    * Main content 
    * emojis 
    * inline attachment link
  * Checklists
  * Attachments
  * Labels TODO
  * Dates? TODO?
  * Comments TODO
  * Activities? TODO?
  * Assignee? TODO?

## TODO:
* Closed Card
* Closed List
* Closed Board

# Exported files hierarchy
* exports
  * [export date and time]
    * Boards.md
    * [Board ID]
      * Board.md
      * Board.json
      * [Card ID]
        * Card.md
        * Card.json
        * Attachments.json
        * Checklists.json
        * Attachments
          * [Attachment ID]
            * [Attachments filename]

```
exports
└── tsconfig.json
│   ├── button.d.ts
│   ├── button.js
│   ├── button.js.map
│   ├── button.stories.d.ts
│   ├── button.stories.js
│   ├── button.stories.js.map
│   ├── index.d.ts
│   ├── index.js
│   └── index.js.map
├── package.json
├── src
│   ├── button.stories.tsx
│   ├── button.tsx
│   └── index.ts
└── tsconfig.json
```
## Recomended MarkDown viewers:
* Mac os: [MacDown](https://macdown.uranusjr.com/)
* Windows: [TOOD](https://github.com/imrezol/trello_exporter)
* Linux: [TODO](https://github.com/imrezol/trello_exporter)

## Trello REST API
* API Introduction: https://developer.atlassian.com/cloud/trello/guides/rest-api/api-introduction/
* The Trello REST API: https://developer.atlassian.com/cloud/trello/rest/


jpackage
https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html#GUID-C1027043-587D-418D-8188-EF8F44A4C06A

https://github.panteleyev.org/jpackage-maven-plugin/

jpackage --win-console --module mymodule --module-path mymodule.jar --type app-image --name myapp

https://stateful.com/blog/trello-api-examples
