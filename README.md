# cim1-gverde-1c2015

Para deploy:

En el root del proyecto, ejecutar:
mvn package appassembler:assemble

Cuando finalice, ir al directorio bin y editar los scripts. Modificar el path de resources, agregando ../resources para que encuentre las properties, el contexto spring etc.