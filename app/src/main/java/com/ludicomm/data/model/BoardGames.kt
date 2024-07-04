package com.ludicomm.data.model

import com.ludicomm.data.model.BoardGame
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgames", strict = false)
data class BoardGames(
    @param:Attribute(name = "termsofuse")
    @get:Attribute(name = "termsofuse")
    val termsOfUseLink: String,

    @param:ElementList(entry = "boardgame", inline = true, required = false)
    @get:ElementList(entry = "boardgame", inline = true, required = false)
    val boardGames: List<BoardGame>? = emptyList()
    )




