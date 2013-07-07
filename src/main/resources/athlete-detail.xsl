<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" />
	<xsl:template match="/">
		<tns:athletes xmlns:tns="http://utils.it.atreceno.com/info-athlete"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://utils.it.atreceno.com/info-athlete info-athlete.xsd ">
			<xsl:for-each select="div[@id='sniffer']/div">
				<tns:athlete>
					<xsl:variable name="bdate"
						select="normalize-space(p[@itemprop='birthDate'])" />
					<xsl:variable name="months"
						select="'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'" />
					<xsl:variable name="forth" select="normalize-space(h3[4])" />
					<tns:index>
						<xsl:value-of select="position()" />
					</tns:index>
					<tns:name>
						<xsl:value-of select="normalize-space(img[1]/@alt)" />
					</tns:name>
					<tns:picture>
						<xsl:value-of select="normalize-space(img[1]/@src)" />
					</tns:picture>
					<tns:country>
						<xsl:value-of select="normalize-space(p[1]/a)" />
					</tns:country>
					<tns:birthDate>
						<xsl:analyze-string select="normalize-space($bdate)"
							regex="([0-9]{{1,2}})\s([A-Z][a-z]+)\s([0-9]{{4}})">
							<xsl:matching-substring>
								<xsl:number value="regex-group(3)" format="0001" />
								<xsl:text>-</xsl:text>
								<xsl:number value="index-of($months, regex-group(2))"
									format="01" />
								<xsl:text>-</xsl:text>
								<xsl:number value="regex-group(1)" format="01" />
							</xsl:matching-substring>
						</xsl:analyze-string>
					</tns:birthDate>
					<xsl:choose>
						<xsl:when test="$forth = 'Height'">
							<tns:height>
								<xsl:value-of select="normalize-space(p[@class='heading-small'][2])" />
							</tns:height>
							<tns:weigth>
								<xsl:value-of select="normalize-space(p[@class='heading-small'][3])" />
							</tns:weigth>
						</xsl:when>
						<xsl:when test="$forth = 'Weight'">
							<tns:height>
								<xsl:value-of select="normalize-space(p[@class='heading-small'][3])" />
							</tns:height>
							<tns:weigth>
								<xsl:value-of select="normalize-space(p[@class='heading-small'][2])" />
							</tns:weigth>
						</xsl:when>
						<xsl:otherwise>
							<tns:height></tns:height>
							<tns:weigth></tns:weigth>
						</xsl:otherwise>
					</xsl:choose>
					<tns:discipline>
						<tns:name>
							<xsl:value-of select="substring-after(ul[1]/li[1]/a/@href, 'sports/')" />
						</tns:name>
						<tns:events>
							<xsl:for-each select="ul[1]/li[position()>1]">
								<tns:event>
									<xsl:value-of select="substring-after(a/@href, 'events/')" />
								</tns:event>
							</xsl:for-each>
						</tns:events>
					</tns:discipline>
				</tns:athlete>
			</xsl:for-each>
		</tns:athletes>
	</xsl:template>
</xsl:stylesheet>  
