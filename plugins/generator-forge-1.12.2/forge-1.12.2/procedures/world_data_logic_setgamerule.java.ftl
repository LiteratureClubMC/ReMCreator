<#if generator.map(field$gamerulesboolean, "gamerules") != "null">
    if(world instanceof World) {
        ((World) world).getGameRules().setOrCreateGameRule("${generator.map(field$gamerulesboolean, "gamerules")}",String.valueOf(${input$gameruleValue}));
    }
</#if>