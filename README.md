#Naughty or Nice
Maintainer: James Ritter

##Build

##Package

##Deploy

##DB Setup - Local
Run mongodb in docker

`sudo docker run -it --hostname MONGODB --name=MONGODB --net=bridge --expose=27017 -v mongodata:/data/db -p 27017:27017 --name mongodb -d mongo`

Exec into mongodb

`docker exec mongodb sh`

Start mongodb CLI

`mongo`

Set db conext to 'non'

`use non`

Insert inital data to create collection and default values

`db.count.insert({naughty: 0, nice: 0})`

####drop non
Selct non DB context

use non

Drop current DB
db.dropDatabase()