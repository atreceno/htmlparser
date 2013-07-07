<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" />
	<xsl:template match="/">
		<xsl:for-each select="div[@id='sniffer']/li[@class]">
			<xsl:value-of select="position()" />
			<xsl:text>&#x9;</xsl:text>
			<xsl:value-of select="normalize-space(a[1])" />
			<xsl:text>&#x9;</xsl:text>
			<xsl:text>http://www.bbc.co.uk</xsl:text>
			<xsl:value-of select="a[1]/@href" />
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>  
