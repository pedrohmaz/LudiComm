package com.ludicomm.data.model

import com.ludicomm.data.model.BoardGame
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "items", strict = false)
data class Collection @JvmOverloads constructor(
    @param:Attribute(name = "totalitems", required = false)
    @get:Attribute(name = "totalitems", required = false)
    val totalItems: String = "",

    @param:Attribute(name = "termsofuse", required = false)
    @get:Attribute(name = "termsofuse", required = false)
    val termsOfUseLink: String = "",

    @param:Attribute(name = "pubdate", required = false)
    @get:Attribute(name = "pubdate", required = false)
    val pubDate: String = "",

    @param:ElementList(entry = "item", inline = true, required = false)
    @get:ElementList(entry = "item", inline = true, required = false)
    val boardGames: List<BoardGame> = emptyList()

)

