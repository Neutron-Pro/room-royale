# Room Royale

Room Royale is a strategic Battle Royale playable directly on Discord using reactions.

Up to 100 players fight across multiple 4-player rooms where observation, anticipation, and survival are the keys to victory.

---

# Features

* Up to 100 players per game
* AI players with multiple difficulty levels
* Fast turn-based gameplay
* Discord reaction-based controls
* Progressive observation system `in coming...`
* Temporary kill-based buffs
* Dynamic room events `in coming...`
* Strategic room movement
* Bluff and mind-game oriented gameplay

---

# Gameplay

Each player starts with:

* 100 HP
* 25 Attack
* 0 Defense
* 3 Potions

Players are divided into rooms of 4 players.

Each turn, players have up to 60 seconds to choose one action:

* ⚔️ Attack
* 🛡️ Defend
* 🧪 Use Potion
* 🚪 Change Room
* 👁️ Observe `in coming...`

If all players choose early, the turn resolves immediately.

The game continues until only one player remains alive.

---

# Observation System `in coming...`

All player stats are hidden by default.

Using the Observe action progressively reveals information about every player in the room:

1. Defense
2. Attack
3. HP
4. Kill count

Observation is permanent during the game.

Attacks become stronger as observation level increases:

```math
Effective Attack = Attack / (5 - Observation Level)
```

---

# Combat System

## Attack

* Base Attack: 25
* Max Attack: 35
* 5% Critical Hit chance
* Critical hits deal double damage

## Defense

* Blocks 100% damage
* After 3 consecutive defenses:

    * only blocks 50%
* 5% chance to counterattack for 10 damage

## Potions

* Fully restore HP
* Players gain +1 potion per kill
* Grants temporary defense bonus

---

# Kill Buff System

Kills grant temporary bonuses:

* +2 Attack
* +2 Defense
* +1 Potion

Bonuses last 5 turns.

Each additional kill before expiration:

* adds +2 turns
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
