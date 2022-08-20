# FarmZones

A Spigot plugin to automatically harvest and replant crops in user-defined zones.

## Command guide
- `/farmzones`
  - Print this help message
- `/farmzones create [name]`
  - Create a new FarmZones farm
- `/farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]`
  - Add zones to a FarmZones farm
- `/farmzones delete farm [farm name]`
  - Delete FarmZones farm and all its zones
- `/farmzones delete zone [farm name] [zone name]`
  - Delete FarmZones zone from a farm
- `/farmzones harvest [farm name]`
  - Harvest a FarmZones farm
- `/farmzones replant [farm name]`
  - Replant a FarmZones farm
- `/farmzones list`
  - Lists all owned FarmZones farms
- `/farmzones detail [farm name]`
  - Lists details of a FarmZones farm

## Player workflow

### Start by creating a farm with whatever name you want.

Name your farm anything you want!

`/farmzones create myfarm`

_Tip: plan your farms ahead of time. Make your farms small enough so you can hold the whole harvest in your inventory without needing to drop any._

### Add as many zones to your farm, each with different crop types.

Each zone has its own "region" which is defined by a first and second position coordinate. Set the coordinate by standing in each of the two coordinates, and all blocks within those coordinates will be within the zone.

`/farmzones add myfarm myzone1 pos1 wheat` - set the first position (first corner)

`/farmzones add myfarm myzone1 pos2 wheat` - set the second position (opposite corner)

_Tip: FarmZones currently supports 5 crop types: wheat, carrot, potato, beetroot, and netherwart._

### Harvest and replant

Reap the rewards!

`/farmzones harvest myfarm` - harvests the farm, instantly giving the results to your inventory. Any excess will be dropped on the ground.

`/farmzones replant myfarm` - replants the farm, taking the necessary seeds from your inventory. Will not replant if you don't have enough seeds, the crop blocks aren't empty, or there isn't farmland under the crop blocks.

### View your farm details

`/farmzones list` - view a listing of all your farms

`/farmzones detail myfarm` - lists detailed information about a farm

## Deploy

Simply download the plugin jar and paste it into your `plugins` directory. FarmZones will handle the rest!

See the config section below for settings you can edit in config.yml.