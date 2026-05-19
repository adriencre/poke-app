package com.example.pokedex.domain.util

object MoveDatabase {
    data class MoveInfo(val type: String, val power: Int?, val accuracy: Int?)

    private val moves = mapOf(
        "tackle" to MoveInfo("normal", 40, 100),
        "growl" to MoveInfo("normal", null, 100),
        "scratch" to MoveInfo("normal", 40, 100),
        "ember" to MoveInfo("fire", 40, 100),
        "water-gun" to MoveInfo("water", 40, 100),
        "vine-whip" to MoveInfo("grass", 45, 100),
        "thunder-shock" to MoveInfo("electric", 40, 100),
        "gust" to MoveInfo("flying", 40, 100),
        "quick-attack" to MoveInfo("normal", 40, 100),
        "bite" to MoveInfo("dark", 60, 100),
        "flamethrower" to MoveInfo("fire", 90, 100),
        "hydro-pump" to MoveInfo("water", 110, 80),
        "solar-beam" to MoveInfo("grass", 120, 100),
        "thunderbolt" to MoveInfo("electric", 90, 100),
        "ice-beam" to MoveInfo("ice", 90, 100),
        "hyper-beam" to MoveInfo("normal", 150, 90),
        "surf" to MoveInfo("water", 90, 100),
        "fly" to MoveInfo("flying", 90, 95),
        "earthquake" to MoveInfo("ground", 100, 100),
        "psychic" to MoveInfo("psychic", 90, 100),
        "slash" to MoveInfo("normal", 70, 100),
        "body-slam" to MoveInfo("normal", 85, 100),
        "double-edge" to MoveInfo("normal", 120, 100),
        "earth-power" to MoveInfo("ground", 90, 100),
        "flame-wheel" to MoveInfo("fire", 60, 100),
        "bubble-beam" to MoveInfo("water", 65, 100),
        "mega-drain" to MoveInfo("grass", 40, 100),
        "giga-drain" to MoveInfo("grass", 75, 100),
        "sludge-bomb" to MoveInfo("poison", 90, 100),
        "shadow-ball" to MoveInfo("ghost", 80, 100),
        "dark-pulse" to MoveInfo("dark", 80, 100),
        "dragon-pulse" to MoveInfo("dragon", 85, 100),
        "dazzling-gleam" to MoveInfo("fairy", 80, 100),
        "iron-head" to MoveInfo("steel", 80, 100),
        "stone-edge" to MoveInfo("rock", 100, 80),
        "bug-buzz" to MoveInfo("bug", 90, 100),
        "aurora-beam" to MoveInfo("ice", 65, 100),
        "sing" to MoveInfo("normal", null, 55),
        "toxic" to MoveInfo("poison", null, 90),
        "thunder-wave" to MoveInfo("electric", null, 90),
        "swords-dance" to MoveInfo("normal", null, null)
    )

    fun getMoveInfo(name: String): MoveInfo {
        val key = name.lowercase().replace(" ", "-")
        return moves[key] ?: inferMoveInfo(key)
    }

    private fun inferMoveInfo(name: String): MoveInfo {
        // Fallback heuristique intelligente basée sur le nom de l'attaque
        val type = when {
            name.contains("fire") || name.contains("flame") || name.contains("blast") || name.contains("burn") -> "fire"
            name.contains("water") || name.contains("hydro") || name.contains("surf") || name.contains("bubble") || name.contains("aqua") -> "water"
            name.contains("thunder") || name.contains("volt") || name.contains("spark") || name.contains("shock") -> "electric"
            name.contains("grass") || name.contains("leaf") || name.contains("vine") || name.contains("seed") || name.contains("spore") || name.contains("mega") || name.contains("giga") -> "grass"
            name.contains("ice") || name.contains("freeze") || name.contains("blizzard") || name.contains("powder") || name.contains("frost") -> "ice"
            name.contains("punch") || name.contains("kick") || name.contains("fighting") || name.contains("karate") || name.contains("double") || name.contains("force") || name.contains("close") -> "fighting"
            name.contains("poison") || name.contains("sludge") || name.contains("acid") || name.contains("toxic") || name.contains("smog") -> "poison"
            name.contains("ground") || name.contains("earth") || name.contains("mud") || name.contains("sand") || name.contains("dig") -> "ground"
            name.contains("fly") || name.contains("wing") || name.contains("aerial") || name.contains("gust") || name.contains("air") || name.contains("hurricane") -> "flying"
            name.contains("psych") || name.contains("mind") || name.contains("zen") || name.contains("teleport") || name.contains("confusion") || name.contains("extrasensory") -> "psychic"
            name.contains("bug") || name.contains("leech") || name.contains("string") || name.contains("signal") || name.contains("cutter") || name.contains("pin") -> "bug"
            name.contains("rock") || name.contains("stone") || name.contains("roll") || name.contains("ancient") || name.contains("headbutt") || name.contains("slide") -> "rock"
            name.contains("ghost") || name.contains("shadow") || name.contains("night") || name.contains("spook") || name.contains("curse") || name.contains("lick") -> "ghost"
            name.contains("dragon") || name.contains("draco") || name.contains("outrage") || name.contains("claw") -> "dragon"
            name.contains("dark") || name.contains("bite") || name.contains("crunch") || name.contains("thief") || name.contains("foul") || name.contains("night") || name.contains("pursuit") -> "dark"
            name.contains("steel") || name.contains("iron") || name.contains("metal") || name.contains("flash") -> "steel"
            name.contains("fairy") || name.contains("dazzle") || name.contains("gleam") || name.contains("charm") || name.contains("play") || name.contains("sweet") -> "fairy"
            else -> "normal"
        }
        val power = when {
            name.contains("punch") || name.contains("slash") || name.contains("claw") || name.contains("fang") || name.contains("strike") -> 75
            name.contains("blast") || name.contains("cannon") || name.contains("pump") || name.contains("storm") || name.contains("beam") || name.contains("hyper") || name.contains("outrage") -> 110
            name.contains("tackle") || name.contains("scratch") || name.contains("pound") || name.contains("whip") || name.contains("slap") || name.contains("sting") || name.contains("peck") || name.contains("gust") -> 40
            name.contains("dance") || name.contains("growl") || name.contains("tail") || name.contains("sing") || name.contains("spore") || name.contains("leer") || name.contains("screen") || name.contains("barrier") || name.contains("mind") || name.contains("teleport") || name.contains("harden") || name.contains("withdraw") || name.contains("toxic") || name.contains("wave") || name.contains("defense") -> null
            else -> 60
        }
        val accuracy = when {
            name.contains("blast") || name.contains("cannon") || name.contains("pump") || name.contains("storm") || name.contains("slam") || name.contains("rush") || name.contains("mega") -> 80
            name.contains("fury") || name.contains("double") || name.contains("triple") || name.contains("comet") -> 85
            name.contains("spore") || name.contains("powder") || name.contains("toxic") || name.contains("sing") || name.contains("screech") || name.contains("hypnosis") -> 75
            else -> 100
        }
        return MoveInfo(type, power, accuracy)
    }
}
