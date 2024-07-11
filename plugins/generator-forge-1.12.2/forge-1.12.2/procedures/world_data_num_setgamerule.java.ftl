<#if generator.map(field$gamerulesnumber, "gamerules") != "null">
    if(world instanceof World) {
        ((World) world).getGameRules().setOrCreateGameRule("${generator.map(field$gamerulesnumber, "gamerules")}",String.valueOf(${input$gameruleValue}));
    }
</#if>