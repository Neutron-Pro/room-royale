# Room Royale

Room Royale is a strategic Battle Royale playable directly on Discord using reactions.

Up to 100 players fight across multiple 4-player rooms where observation, anticipation, and survival are the keys to victory.

---

# Features

* Up to 100 players per game
* AI players with multiple difficulty levels
* Fast turn-based gameplay
* Discord reaction-based controls
* Progressive observation system
* Temporary kill-based buffs
* Dynamic room events `in coming...`
* Strategic room movement
* Bluff and mind-game oriented gameplay

---

# Gameplay

Each player starts with:

* 100/100 HP
* 20 Attack
* 0 Defense
* 1 Potions
* 0/100 Energy points

Players are divided into rooms of 4 players.

Each turn, players have up to 60 seconds to choose one action:

* ⚔️ Attack
* ⚔️ Attack Special
* 🛡️ Defend
* 🧪 Use Potion
* 🚪 Change Room
* 👁️ Observe

If all players choose early, the turn resolves immediately.

The game continues until only one player remains alive.

---

# Observation System

All player stats are hidden by default.

Using the Observe action progressively reveals information about every player in the room:

1. Attack
2. Defense
3. Kill count
4. HP

Observation is permanent during the game.

Attacks become stronger as observation level increases:

Without attack special :
```math
Effective damage = (Attack - Defense) * ((20 + (clamp(observation, 0, 4) * 20)) / 100)
```

With attack special :
```math
Effective damage = (Attack - Defense) * 2
```

With critique : 
```math
Effective damage * 2
```

---

# Combat System

## Attack

* Base Attack: 25
* Max Attack: 35
* 5% Critical Hit chance
* Critical hits deal double damage
* 50% of damage dealt is converted to energy.

## Attack Special

* Identical of Attack but x2
* Require full energy for use it

## Defense

* Blocks 100% damage
  * 100% of damage dealt is converted to energy.
* After 3 consecutive defenses:
    * only blocks 50% of damages
    * 0 energy points earned.

## Potions

* Fully restore HP
* Players gain +1 potion per kill
* Grants temporary defense bonus

## Observation

* +1 observation level for each entity of the room
* +10 energy points.

---

# Kill Buff System

Kills grant temporary bonuses:

* +2 Attack
* +2 Defense
* +1 Potion

Bonuses last 5 turns.

Each additional kill before expiration:

* adds +4 turns
* stacks bonuses

Stats reset if the timer reaches 0.

---

# Rooms

Players can move between rooms.

Rules:

* A player can only move to rooms containing 1 to 3 players
* Empty rooms are permanently closed
* Room events can dynamically affect gameplay

---

# AI System

Three AI difficulty levels:

* Beginner
* Normal
* Expert

Each AI type uses different strategies and behaviors.

---

# Philosophy

Room Royale focuses on:

* Simplicity
* Fast gameplay
* Strategic depth
* Information management
* Bluff and anticipation

The game is designed to stay easy to play while offering meaningful tactical decisions.

---

# Status

Room Royale V2 is currently in development.

---

# License

MIT License
