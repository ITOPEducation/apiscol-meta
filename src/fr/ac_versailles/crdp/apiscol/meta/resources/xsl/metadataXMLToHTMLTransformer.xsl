<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lom="http://ltsc.ieee.org/xsd/LOM"
	xmlns:lomfr="http://www.lom-fr.fr/xsd/LOMFR" xmlns:scolomfr="http://www.lom-fr.fr/xsd/SCOLOMFR"
	xmlns:apiscol="http://www.crdp.ac-versailles.fr/2012/apiscol" xmlns:fb="http://www.facebook.com/2008/fbml"
	exclude-result-prefixes="#default">
	<xsl:param name="version" select="'0.0.0'" />
	<xsl:param name="standalone" select="'true'" />
	<xsl:param name="language" select="'all'" />
	<xsl:param name="mode" select="'full'" />
	<xsl:param name="style" select="'redmond'" />
	<xsl:param name="device" select="'auto'" />
	<xsl:output method="html" omit-xml-declaration="yes"
		encoding="UTF-8" indent="yes" />
	<xsl:variable name="cdn"
		select="'http://apiscol.cdn.local/cdn/'" />
	<xsl:variable name="icon-base" select="'icons/st0'" />
	<xsl:template match="/">
		<xsl:choose>
			<xsl:when test="$standalone='true'">
				<html>
					<head>
						<meta charset="utf-8">
						</meta>
						<xsl:element name="meta" namespace="">
							<xsl:attribute name="name">
						<xsl:value-of select="'description'"></xsl:value-of>
						</xsl:attribute>
							<xsl:attribute name="content">
						<xsl:value-of
								select="//*[local-name()='general']/*[local-name()='description']"></xsl:value-of>
						</xsl:attribute>
						</xsl:element>
						<meta name="viewport" content="width=device-width"></meta>
						<style>
							.apiscol-notice {
							width:800px;
							margin: auto
							auto;
							}
						</style>

						<title>
							<xsl:value-of
								select="//*[local-name()='general']/*[local-name()='title']"></xsl:value-of>
						</title>
					</head>
					<body>
						<xsl:apply-templates select="*[local-name()='lom']"></xsl:apply-templates>
						<script
							src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.js">
							<xsl:text>
</xsl:text>
						</script>
						<xsl:element name="script" namespace="">
							<xsl:attribute name="src">
						<xsl:value-of select="concat($cdn,$version, '/js/jquery.apiscol.js')"></xsl:value-of>
						</xsl:attribute>
							<xsl:text>  </xsl:text>
						</xsl:element>
					</body>
				</html>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="*[local-name()='lom']"></xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="*[local-name()='lom']">
		<xsl:variable name="apos">
			<xsl:text>'</xsl:text>
		</xsl:variable>
		<xsl:element name="div" namespace="">
			<xsl:attribute name="class">
			<xsl:value-of select="'apiscol-notice'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="data-mode">
			<xsl:value-of select="$mode"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="data-style">
			<xsl:value-of select="$style"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="data-device">
			<xsl:value-of select="$device"></xsl:value-of>
			</xsl:attribute>
			<xsl:element name="style" namespace="">
				<xsl:attribute name="scoped"></xsl:attribute>
				<xsl:text disable-output-escaping="yes">.apiscol-notice>*:not(.waiter){opacity:0;}</xsl:text>
			</xsl:element>
			<xsl:element name="header" namespace="">
				<xsl:element name="h1" namespace="">
					<xsl:value-of select="*[local-name()='general']/*[local-name()='title']"></xsl:value-of>
				</xsl:element>
				<xsl:choose>
					<xsl:when test="*[local-name()='technical']/*[local-name()='location']">
						<xsl:element name="a" namespace="">
							<xsl:attribute name="class">download</xsl:attribute>
							<xsl:attribute name="href">
						<xsl:value-of
								select="*[local-name()='technical']/*[local-name()='location']"></xsl:value-of>
						</xsl:attribute>
							Accéder
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="a">
							<xsl:attribute name="class">download-unavailable</xsl:attribute>
							Ressource indisponible
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>

			</xsl:element>
			<xsl:element name="div" namespace="">
				<xsl:attribute name="class"><xsl:value-of select="'main-content'"></xsl:value-of></xsl:attribute>
				<xsl:element name="nav" namespace="">
					<xsl:element name="ul" namespace="">

						<xsl:element name="li" namespace="">
							<xsl:element name="a" namespace="">
								<xsl:attribute name="href">
							<xsl:value-of select="'#presentation'"></xsl:value-of>
							</xsl:attribute>
								Présentation
							</xsl:element>
						</xsl:element>
						<xsl:element name="li" namespace="">
							<xsl:element name="a" namespace="">
								<xsl:attribute name="href">
							<xsl:value-of select="'#educational'"></xsl:value-of>
							</xsl:attribute>
								Pédagogie
							</xsl:element>
						</xsl:element>
						<xsl:element name="li" namespace="">
							<xsl:element name="a" namespace="">
								<xsl:attribute name="href">
							<xsl:value-of select="'#technical'"></xsl:value-of>
							</xsl:attribute>
								Technique
							</xsl:element>
						</xsl:element>
						<xsl:element name="li" namespace="">
							<xsl:element name="a" namespace="">
								<xsl:attribute name="href">
							<xsl:value-of select="'#rights'"></xsl:value-of>
							</xsl:attribute>
								Droits
							</xsl:element>
						</xsl:element>

					</xsl:element>
				</xsl:element>
				<xsl:element name="div" namespace="">
					<xsl:attribute name="id">
				<xsl:value-of select="'presentation'"></xsl:value-of>
				</xsl:attribute>
					<xsl:element name="header" namespace="">
						<xsl:element name="section" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'icon-container'"></xsl:value-of>
							</xsl:attribute>
							<xsl:apply-templates
								select="*[local-name()='general']/*[local-name()='generalResourceType']" />
							<xsl:apply-templates
								select="*[local-name()='educational']/*[local-name()='learningResourceType']">
								<xsl:with-param name="mode" select="'compact'" />
							</xsl:apply-templates>
							<xsl:apply-templates
								select="*[local-name()='educational']/*[local-name()='intendedEndUserRole']">
								<xsl:with-param name="mode" select="'compact'" />
							</xsl:apply-templates>
							<xsl:apply-templates
								select="*[local-name()='technical']/*[local-name()='format']">
								<xsl:with-param name="mode" select="'compact'" />
							</xsl:apply-templates>
						</xsl:element>
						<xsl:element name="section" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'share-buttons'"></xsl:value-of>
							</xsl:attribute>
							<xsl:variable name="link">
								<xsl:value-of
									select="//*[local-name()='metaMetadata']/*[local-name()='identifier']/*[local-name()='entry']"></xsl:value-of>
							</xsl:variable>
							<xsl:variable name="title">
								<xsl:value-of
									select="//*[local-name()='general']/*[local-name()='title']"></xsl:value-of>
							</xsl:variable>
							<xsl:variable name="desc">
								<xsl:value-of
									select="//*[local-name()='general']/*[local-name()='description']"></xsl:value-of>
							</xsl:variable>
							<xsl:variable name="encoded-title">
								<xsl:call-template name="url-encode">
									<xsl:with-param name="str">
										<xsl:value-of select="normalize-space($title)"></xsl:value-of>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
							<xsl:variable name="encoded-desc">
								<xsl:call-template name="url-encode">
									<xsl:with-param name="str">
										<xsl:value-of select="normalize-space($desc)"></xsl:value-of>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
							<xsl:element name="div" namespace="">
								<xsl:call-template name="share-link">
									<xsl:with-param name="label">
										<xsl:value-of select="'ApiScol'"></xsl:value-of>
									</xsl:with-param>
									<xsl:with-param name="href">
										<xsl:value-of select="concat($link, '/snippet')"></xsl:value-of>
									</xsl:with-param>
									<xsl:with-param name="icon">
										<xsl:value-of select="'logo-api'"></xsl:value-of>
									</xsl:with-param>
								</xsl:call-template>
								<xsl:element name="button" namespace="">
									Partager
								</xsl:element>
							</xsl:element>
							<xsl:element name="ul" namespace="">
								<xsl:attribute name="id"><xsl:value-of
									select="'share-options'"></xsl:value-of> </xsl:attribute>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Del.icio.us'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://delicious.com/save?url=', $link, '&amp;title=' , $encoded-title)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'delicious'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Digg'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://digg.com/submit?phase=2&amp;url=', $link, '&amp;title=' , $encoded-title)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'digg'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Facebook'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://www.facebook.com/sharer/sharer.php?u=', $link, '&amp;t=' , $encoded-title)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'facebook'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Google_bookmarks'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://www.google.com/bookmarks/mark?op=edit&amp;bkmk=', $link, '&amp;title=' , $encoded-title)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'google'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Identi.ca'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://identi.ca/notice/new?status_textarea=', $link)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'identica'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>

								</xsl:element>
								<xsl:element name="li" namespace="">
									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'netvibes'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://www.netvibes.com/subscribe.php?url=', $link)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'netvibes'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>
								<xsl:element name="li" namespace="">

									<xsl:call-template name="share-link">
										<xsl:with-param name="label">
											<xsl:value-of select="'Twitter'"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="href">
											<xsl:value-of
												select="concat('http://twitter.com/share?text=',$encoded-title,'&amp;url=',$link)"></xsl:value-of>
										</xsl:with-param>
										<xsl:with-param name="icon">
											<xsl:value-of select="'twitter'"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:element>

							</xsl:element>



						</xsl:element>
						<xsl:element name="section" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'level-container'"></xsl:value-of>
							</xsl:attribute>
							<xsl:apply-templates select="*[local-name()='classification']">
								<xsl:with-param name="purpose"
									select="concat('domaine d',$apos,'enseignement' )" />
							</xsl:apply-templates>
							<xsl:element name="br" namespace=""></xsl:element>
							<xsl:apply-templates select="*[local-name()='classification']">
								<xsl:with-param name="purpose" select="'educational level'" />
							</xsl:apply-templates>
						</xsl:element>
						<xsl:element name="section" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'authors'"></xsl:value-of>
							</xsl:attribute>
							<xsl:apply-templates
								select="*[local-name()='lifeCycle']/*[local-name()='contribute']">
								<xsl:with-param name="mode" select="'compact'" />
							</xsl:apply-templates>
						</xsl:element>
					</xsl:element>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'desc-container'"></xsl:value-of>
							</xsl:attribute>

						<xsl:apply-templates select="*[local-name()='relation']">
							<xsl:with-param name="kind" select="'a pour vignette'" />
						</xsl:apply-templates>
						<xsl:element name="span" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'description'"></xsl:value-of>
							</xsl:attribute>
							<xsl:element name="span" namespace="">
								<xsl:element name="strong" namespace="">
									Description :
								</xsl:element>
								<xsl:value-of
									select="//*[local-name()='general']/*[local-name()='description']"></xsl:value-of>
							</xsl:element>
						</xsl:element>

					</xsl:element>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'preview-container'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="div" namespace="">
							<xsl:attribute name="class">
								<xsl:value-of select="'preview-area'"></xsl:value-of>
							</xsl:attribute>
							<xsl:attribute name="style">
								<xsl:value-of select="'height:0'"></xsl:value-of>
							</xsl:attribute>
							<xsl:apply-templates select="*[local-name()='relation']">
								<xsl:with-param name="kind" select="'a pour aperçu'" />
							</xsl:apply-templates>
						</xsl:element>
					</xsl:element>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'classifications'"></xsl:value-of>
							</xsl:attribute>
						<xsl:apply-templates select="*[local-name()='classification']">
							<xsl:with-param name="purpose" select="'idea'" />
						</xsl:apply-templates>
						<xsl:apply-templates
							select="//*[local-name()='general']/*[local-name()='keyword']"></xsl:apply-templates>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div" namespace="">
					<xsl:attribute name="id">
				<xsl:value-of select="'educational'"></xsl:value-of>
				</xsl:attribute>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'summary'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="ul" namespace="">
							<xsl:apply-templates
								select="*[local-name()='educational']/*[local-name()='learningResourceType']">
								<xsl:with-param name="mode" select="'explicit'" />
							</xsl:apply-templates>
							<xsl:apply-templates
								select="*[local-name()='educational']/*[local-name()='intendedEndUserRole']">
								<xsl:with-param name="mode" select="'explicit'" />
							</xsl:apply-templates>
							<xsl:apply-templates
								select="*[local-name()='technical']/*[local-name()='format']">
								<xsl:with-param name="mode" select="'explicit'" />
							</xsl:apply-templates>
						</xsl:element>
						<xsl:apply-templates
							select="*[local-name()='educational']/*[local-name()='typicalLearningTime']/*[local-name()='duration']">
						</xsl:apply-templates>
					</xsl:element>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'details'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="ul" namespace="">
							<xsl:apply-templates
								select="*[local-name()='educational']/*[local-name()='description']">
							</xsl:apply-templates>
						</xsl:element>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div" namespace="">
					<xsl:attribute name="id">
				<xsl:value-of select="'technical'"></xsl:value-of>
				</xsl:attribute>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'summary'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="ul" namespace="">
							<xsl:apply-templates
								select="*[local-name()='technical']/*[local-name()='format']">
								<xsl:with-param name="mode" select="'explicit'" />
							</xsl:apply-templates>

						</xsl:element>
						<xsl:apply-templates
							select="*[local-name()='technical']/*[local-name()='duration']/*[local-name()='duration']">
						</xsl:apply-templates>
					</xsl:element>
					<xsl:element name="section" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'details'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="ul" namespace="">
							<xsl:apply-templates
								select="*[local-name()='technical']/*[local-name()='installationRemarks' or local-name()='otherPlatformRequirements']">
							</xsl:apply-templates>
						</xsl:element>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div" namespace="">
					<xsl:attribute name="id">
				<xsl:value-of select="'rights'"></xsl:value-of>
				</xsl:attribute>
					<xsl:element name="div" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'contributors'"></xsl:value-of>
							</xsl:attribute>
						<xsl:element name="ul" namespace="">
							<xsl:apply-templates
								select="*[local-name()='lifeCycle']/*[local-name()='contribute']">
								<xsl:with-param name="mode" select="'explicit'" />
							</xsl:apply-templates>

						</xsl:element>
					</xsl:element>
					<xsl:element name="div" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'licence'"></xsl:value-of>
							</xsl:attribute>
						<xsl:apply-templates
							select="*[local-name()='rights']/*[local-name()='copyrightAndOtherRestrictions']/*[local-name()='value']">
						</xsl:apply-templates>
						<xsl:apply-templates
							select="*[local-name()='rights']/*[local-name()='cost']/*[local-name()='value']">
						</xsl:apply-templates>
						<xsl:apply-templates
							select="*[local-name()='rights']/*[local-name()='description']">
						</xsl:apply-templates>
					</xsl:element>
				</xsl:element>
			</xsl:element>

		</xsl:element>


	</xsl:template>
	<xsl:template match="//*[local-name()='general']/*[local-name()='title']">
		<xsl:call-template name="best-string-for-language"></xsl:call-template>
	</xsl:template>
	<xsl:template
		match="//*[local-name()='general']/*[local-name()='description']">
		<xsl:call-template name="best-string-for-language"></xsl:call-template>
	</xsl:template>
	<xsl:template match="//*[local-name()='general']/*[local-name()='keyword']">
		<xsl:element name="span" namespace="">
			<xsl:attribute name="class"><xsl:value-of select="'tag'"></xsl:value-of> </xsl:attribute>
			<xsl:call-template name="best-string-for-language"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="best-string-for-language">
		<xsl:choose>
			<xsl:when test="*[local-name()='string'][@language=$language]">
				<xsl:value-of disable-output-escaping="yes"
					select="*[local-name()='string'][@language=$language]"></xsl:value-of>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of disable-output-escaping="yes"
					select="*[local-name()='string'][1]"></xsl:value-of>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="*[local-name()='general']/*[local-name()='generalResourceType']">
		<xsl:variable name="generalresourcetype">
			<xsl:value-of select="*[local-name()='value']"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:value-of
				select="concat('general.scolomfr:generalResourceType.', $generalresourcetype)"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="img" namespace="">
			<xsl:attribute name="id">
			<xsl:value-of select="concat(translate($key, ' ', '_'),'_' ,position())"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="class">
			<xsl:value-of select="'metadata-icon'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="width">
			<xsl:value-of select="'32'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="height">
			<xsl:value-of select="'32'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="src">
			<xsl:value-of select="$uri"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="title">
			<xsl:value-of select="$tooltip"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="alt">
			<xsl:value-of select="$tooltip"></xsl:value-of>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<xsl:template
		match="*[local-name()='educational']/*[local-name()='learningResourceType']">
		<xsl:param name="mode"></xsl:param>
		<xsl:variable name="learningresourcetype">
			<xsl:value-of select="*[local-name()='value']"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:value-of
				select="concat('educational.learningResourceType.', $learningresourcetype)"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$mode='compact'">
				<xsl:call-template name="create-metadata-icon">
					<xsl:with-param name="id">
						<xsl:value-of select="$key"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="alt">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="title">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="src">
						<xsl:value-of select="$uri"></xsl:value-of>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$mode='explicit'">
				<xsl:element name="li" namespace="">
					<xsl:call-template name="create-metadata-icon">
						<xsl:with-param name="id">
							<xsl:value-of select="$key"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="alt">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="title">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="src">
							<xsl:value-of select="$uri"></xsl:value-of>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:element name="span" namespace="">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:element>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="//*[local-name()='educational']/*[local-name()='description']">
		<xsl:element name="li" namespace="">
			<xsl:attribute name="class"><xsl:value-of
				select="'educational-description'"></xsl:value-of> </xsl:attribute>

			<xsl:call-template name="best-string-for-language"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="//*[local-name()='technical']/*[local-name()='installationRemarks' or local-name()='otherPlatformRequirements']">
		<xsl:element name="li" namespace="">
			<xsl:call-template name="best-string-for-language"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	<xsl:template name="create-metadata-icon">
		<xsl:param name="id"></xsl:param>
		<xsl:param name="src"></xsl:param>
		<xsl:param name="alt"></xsl:param>
		<xsl:param name="title"></xsl:param>
		<xsl:element name="img" namespace="">
			<xsl:attribute name="class">
			<xsl:value-of select="concat(translate($id, ' ', '_'),' metadata-icon' )"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="width">
			<xsl:value-of select="'32'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="height">
			<xsl:value-of select="'32'"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="src">
			<xsl:value-of select="$src"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="title">
			<xsl:value-of select="$title"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="alt">
			<xsl:value-of select="$alt"></xsl:value-of>
			</xsl:attribute>
		</xsl:element>

	</xsl:template>
	<xsl:template
		match="*[local-name()='educational']/*[local-name()='intendedEndUserRole']">
		<xsl:param name="mode"></xsl:param>
		<xsl:variable name="intendedenduserrole">
			<xsl:value-of select="*[local-name()='value']"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:value-of
				select="concat('educational.intendedEndUserRole.', $intendedenduserrole)"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$mode='compact'">
				<xsl:call-template name="create-metadata-icon">
					<xsl:with-param name="id">
						<xsl:value-of select="$key"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="alt">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="title">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="src">
						<xsl:value-of select="$uri"></xsl:value-of>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$mode='explicit'">
				<xsl:element name="li" namespace="">
					<xsl:call-template name="create-metadata-icon">
						<xsl:with-param name="id">
							<xsl:value-of select="$key"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="alt">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="title">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="src">
							<xsl:value-of select="$uri"></xsl:value-of>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:element name="span" namespace="">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:element>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="*[local-name()='educational']/*[local-name()='typicalLearningTime']/*[local-name()='duration']">
		<xsl:element name="span" namespace="">
			<xsl:attribute name="class">
	<xsl:value-of select="'duration'"></xsl:value-of>
	</xsl:attribute>
			Durée :
			<xsl:element name="span" namespace="">
				<xsl:value-of select="."></xsl:value-of>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="*[local-name()='technical']/*[local-name()='duration']/*[local-name()='duration']">
		<xsl:element name="span" namespace="">
			<xsl:attribute name="class">
	<xsl:value-of select="'duration'"></xsl:value-of>
	</xsl:attribute>
			Durée :
			<xsl:element name="span" namespace="">
				<xsl:value-of select="."></xsl:value-of>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='technical']/*[local-name()='format']">
		<xsl:param name="mode"></xsl:param>
		<xsl:variable name="format">
			<xsl:value-of select="."></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:value-of select="concat('technical.format.', $format)"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key" select="$key"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$mode='compact'">
				<xsl:call-template name="create-metadata-icon">
					<xsl:with-param name="id">
						<xsl:value-of select="$key"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="alt">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="title">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:with-param>
					<xsl:with-param name="src">
						<xsl:value-of select="$uri"></xsl:value-of>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$mode='explicit'">
				<xsl:element name="li" namespace="">
					<xsl:call-template name="create-metadata-icon">
						<xsl:with-param name="id">
							<xsl:value-of select="$key"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="alt">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="title">
							<xsl:value-of select="$tooltip"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="src">
							<xsl:value-of select="$uri"></xsl:value-of>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:element name="span" namespace="">
						<xsl:value-of select="$tooltip"></xsl:value-of>
					</xsl:element>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="*[local-name()='rights']/*[local-name()='copyrightAndOtherRestrictions']/*[local-name()='value']">
		<xsl:variable name="value">
			<xsl:value-of select="."></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:choose>
				<xsl:when test="contains($value, 'yes')">
					<xsl:value-of select="'rights.copyrightAndOtherRestrictions.yes'"></xsl:value-of>
				</xsl:when>
				<xsl:when test="contains($value, 'no')">
					<xsl:value-of select="'rights.copyrightAndOtherRestrictions.no'"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'rights.copyrightAndOtherRestrictions.unknown'"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="div" namespace="">
			<xsl:attribute name="class">
								<xsl:value-of select="'copyright'"></xsl:value-of>
							</xsl:attribute>
			<xsl:call-template name="create-metadata-icon">
				<xsl:with-param name="id">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="alt">
					<xsl:value-of select="$tooltip"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="title">
					<xsl:value-of select="$tooltip"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="src">
					<xsl:value-of select="$uri"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:element name="span" namespace="">
				<xsl:element name="strong" namespace="">
					Copyright :
				</xsl:element>
				<xsl:choose>
					<xsl:when test="contains($value, 'yes')">
						<xsl:value-of select="'oui'"></xsl:value-of>
					</xsl:when>
					<xsl:when test="contains($value, 'no')">
						<xsl:value-of select="'non'"></xsl:value-of>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'Inconnu'"></xsl:value-of>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="*[local-name()='rights']/*[local-name()='cost']/*[local-name()='value']">
		<xsl:variable name="value">
			<xsl:value-of select="."></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="key">
			<xsl:choose>
				<xsl:when test="contains($value, 'yes')">
					<xsl:value-of select="'rights.cost.yes'"></xsl:value-of>
				</xsl:when>
				<xsl:when test="contains($value, 'no')">
					<xsl:value-of select="'rights.cost.no'"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'rights.cost.unknown'"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="uri">
			<xsl:call-template name="geturi">
				<xsl:with-param name="key">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="tooltip">
			<xsl:call-template name="gettooltip">
				<xsl:with-param name="key">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="div" namespace="">
			<xsl:attribute name="class">
								<xsl:value-of select="'cost'"></xsl:value-of>
							</xsl:attribute>
			<xsl:call-template name="create-metadata-icon">
				<xsl:with-param name="id">
					<xsl:value-of select="$key"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="alt">
					<xsl:value-of select="$tooltip"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="title">
					<xsl:value-of select="$tooltip"></xsl:value-of>
				</xsl:with-param>
				<xsl:with-param name="src">
					<xsl:value-of select="$uri"></xsl:value-of>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:element name="span" namespace="">
				<xsl:element name="strong" namespace="">
					Gratuit :
				</xsl:element>
				<xsl:choose>
					<xsl:when test="contains($value, 'yes')">
						<xsl:value-of select="'non'"></xsl:value-of>
					</xsl:when>
					<xsl:when test="contains($value, 'no')">
						<xsl:value-of select="'oui'"></xsl:value-of>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'Non précisé'"></xsl:value-of>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='rights']/*[local-name()='description']">
		<xsl:element name="div" namespace="">
			<xsl:attribute name="class">
								<xsl:value-of select="'rights-description'"></xsl:value-of>
							</xsl:attribute>
			<xsl:call-template name="best-string-for-language"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='classification']">
		<xsl:param name="purpose"></xsl:param>
		<xsl:variable name="apos">
			<xsl:text>'</xsl:text>
		</xsl:variable>
		<xsl:choose>
			<xsl:when
				test="(*[local-name()='purpose']/*[local-name()='value']=concat('domaine d',$apos,'enseignement') and $purpose=concat('domaine d',$apos,'enseignement' )) or (*[local-name()='purpose']/*[local-name()='value']='educational level' and $purpose='educational level')">
				<xsl:for-each select="*[local-name()='taxonPath']">
					<xsl:element name="h2" namespace="">
						<xsl:attribute name="data-purpose"><xsl:value-of
							select="$purpose"></xsl:value-of> </xsl:attribute>
						<xsl:for-each select="*[local-name()='taxon']">
							<xsl:if test="position()=last()">
								<xsl:attribute name="data-taxon"><xsl:value-of
									select="concat(*[local-name()='source']/*[local-name()='string'],'|_|', *[local-name()='id'])"></xsl:value-of>
			</xsl:attribute>
								<xsl:variable name="label"
									select="*[local-name()='entry']/*[local-name()='string']"></xsl:variable>
								<xsl:value-of select="$label"></xsl:value-of>
							</xsl:if>
						</xsl:for-each>
						<xsl:text>&#0160;&#0160;&#0160;&#0160;</xsl:text>

					</xsl:element>
				</xsl:for-each>
			</xsl:when>
			<xsl:when
				test="*[local-name()='purpose']/*[local-name()='value']='idea' and $purpose='idea'">
				<xsl:element name="span" namespace="">
					<xsl:attribute name="data-purpose"><xsl:value-of
						select="'idea'"></xsl:value-of> </xsl:attribute>
					<xsl:attribute name="class"><xsl:value-of
						select="'tag'"></xsl:value-of> </xsl:attribute>
					<xsl:attribute name="data-taxon"><xsl:value-of
						select="concat(*[local-name()='taxonPath']/*[local-name()='source']/*[local-name()='string'],'|_|', *[local-name()='taxonPath']/*[local-name()='taxon']/*[local-name()='id'])"></xsl:value-of>
			</xsl:attribute>
					<xsl:variable name="label"
						select="*[local-name()='taxonPath']/*[local-name()='taxon']/*[local-name()='entry']/*[local-name()='string']"></xsl:variable>
					<xsl:value-of select="$label"></xsl:value-of>
					<xsl:value-of select="' '"></xsl:value-of>
				</xsl:element>
			</xsl:when>
		</xsl:choose>



	</xsl:template>
	<xsl:template match="*[local-name()='relation']">
		<xsl:param name="kind"></xsl:param>
		<xsl:choose>
			<xsl:when
				test="*[local-name()='kind']/*[local-name()='value']='a pour vignette' and $kind='a pour vignette'">
				<xsl:variable name="thumbsrc">
					<xsl:value-of
						select="*[local-name()='resource']/*[local-name()='identifier']/*[local-name()='entry']"></xsl:value-of>
				</xsl:variable>
				<xsl:if test="not(normalize-space($thumbsrc)='')">
					<xsl:element name="div" namespace="">
						<xsl:attribute name="class">
							<xsl:value-of select="'thumb'"></xsl:value-of>
							</xsl:attribute>

						<xsl:element name="img" namespace="">
							<xsl:attribute name="src"><xsl:value-of
								select="$thumbsrc"></xsl:value-of> </xsl:attribute>
						</xsl:element>
					</xsl:element>
				</xsl:if>

			</xsl:when>
			<xsl:when
				test="*[local-name()='kind']/*[local-name()='value']='a pour aperçu' and $kind='a pour aperçu'">
				<xsl:element name="a" namespace="">
					<xsl:attribute name="href"><xsl:value-of
						select="*[local-name()='resource']/*[local-name()='identifier']/*[local-name()='entry']"></xsl:value-of> </xsl:attribute>
					Prévisualisation
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="*[local-name()='lifeCycle']/*[local-name()='contribute']">
		<xsl:param name="mode"></xsl:param>
		<xsl:variable name="role">

			<xsl:value-of select="*[local-name()='role']/*[local-name()='value']"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="date">
			<xsl:value-of select="*[local-name()='date']/*[local-name()='dateTime']"></xsl:value-of>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$mode='compact'">
				<xsl:element name="span" namespace="">
					<xsl:attribute name="data-role">
			<xsl:value-of select="$role">
			</xsl:value-of>
			</xsl:attribute>
					<xsl:attribute name="class">
			<xsl:value-of select="'label'">
			</xsl:value-of>
			</xsl:attribute>
					<xsl:attribute name="data-date">					
					<xsl:if test="$date !=''">	
					<xsl:value-of select="$date">
					</xsl:value-of>
					</xsl:if>
			</xsl:attribute>

					<xsl:variable name="vcardstring">
						<xsl:value-of select="*[local-name()='entity']"></xsl:value-of>
					</xsl:variable>
					<xsl:call-template name="extract-param-from-vcard">
						<xsl:with-param name="vcard">
							<xsl:value-of select="$vcardstring"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="paramname" select="'FN'"></xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="extract-param-from-vcard">
						<xsl:with-param name="vcard">
							<xsl:value-of select="$vcardstring"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="paramname" select="'ORG'"></xsl:with-param>
					</xsl:call-template>


				</xsl:element>
			</xsl:when>
			<xsl:when test="$mode='explicit'">
				<xsl:element name="li" namespace="">
					<xsl:attribute name="class">
								<xsl:value-of select="'role'"></xsl:value-of>
							</xsl:attribute>
					<xsl:element name="span" namespace="">
						<xsl:attribute name="class">
								<xsl:value-of select="'role-label'"></xsl:value-of>
							</xsl:attribute>
						<xsl:call-template name="getroleinuserlanguage">
							<xsl:with-param name="role">
								<xsl:value-of select="$role"></xsl:value-of>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:element>
					<xsl:element name="ul" namespace="">
						<xsl:element name="li" namespace="">
							<xsl:variable name="vcardstring">
								<xsl:value-of select="*[local-name()='entity']"></xsl:value-of>
							</xsl:variable>
							<xsl:call-template name="extract-param-from-vcard">
								<xsl:with-param name="vcard">
									<xsl:value-of select="$vcardstring"></xsl:value-of>
								</xsl:with-param>
								<xsl:with-param name="paramname" select="'FN'"></xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="extract-param-from-vcard">
								<xsl:with-param name="vcard">
									<xsl:value-of select="$vcardstring"></xsl:value-of>
								</xsl:with-param>
								<xsl:with-param name="paramname" select="'ORG'"></xsl:with-param>
							</xsl:call-template>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:when>
		</xsl:choose>

	</xsl:template>
	<xsl:template name="gettooltip">
		<xsl:param name="key" />
		<xsl:choose>
			<xsl:when
				test="$key='general.scolomfr:generalResourceType.présentation multimédia'">
				<xsl:value-of select="'Présentation multimédia'"></xsl:value-of>
			</xsl:when>
			<xsl:when
				test="$key='general.scolomfr:generalResourceType.présentation multimédia'">
				<xsl:value-of select="'Présentation grotesque'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='general.scolomfr:generalResourceType.diaporama'">
				<xsl:value-of select="'Diaporama'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='general.scolomfr:generalResourceType.film'">
				<xsl:value-of select="'Vidéo'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.learningResourceType.exercise'">
				<xsl:value-of select="'Exercice'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.learningResourceType.lecture'">
				<xsl:value-of select="'Cours'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.learningResourceType.animation'">
				<xsl:value-of select="'Animation'"></xsl:value-of>
			</xsl:when>
			<xsl:when
				test="$key='educational.learningResourceType.scénario pédagogique'">
				<xsl:value-of select="'Scénario pédagogique
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.learningResourceType.guide'">
				<xsl:value-of select="'Guide'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.intendedEndUserRole.teacher'">
				<xsl:text disable-output-escaping="yes"><![CDATA[Ressource pour l&rsquo;enseignant]]></xsl:text>
			</xsl:when>
			<xsl:when test="$key='educational.intendedEndUserRole.learner'">
				<xsl:text disable-output-escaping="yes"><![CDATA[Ressource pour l&rsquo;élève]]></xsl:text>
			</xsl:when>
			<xsl:when test="$key='educational.typicallearningtime.duration'">
			</xsl:when>
			<xsl:when test="$key='educational.scolomfr:place.en salle de classe'">
			</xsl:when>
			<xsl:when test="$key='educational.scolomfr:tool.TBI'">
				<xsl:value-of select="'Tableau numérique
				interactif
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='educational.scolomfr:tool.tablette informatique'">
				<xsl:value-of select="'Tablette numérique
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.text/html'">
				<xsl:value-of select="'Page web'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.text/rtf'">
				<xsl:value-of select="'RTF'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.application/vnd.ms-powerpoint'">
				<xsl:value-of select="'PowerPoint
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.application/pdf'">
				<xsl:value-of select="'PDF'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.application/x-uniboard+zip'">
				<xsl:value-of select="'Open Sankoré
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.image/jpeg'">
				<xsl:value-of select="'Image JPEG
			'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.image/gif'">
				<xsl:value-of select="'Image GIF'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.image/png'">
				<xsl:value-of select="'Image PNG'"></xsl:value-of>
			</xsl:when>

			<xsl:when test="$key='technical.format.image/bmp'">
				<xsl:value-of select="'Image BMP'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.image/tiff'">
				<xsl:value-of select="'Image TIFF'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.image/svg'">
				<xsl:value-of select="'Image SVG'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/flv'">
				<xsl:value-of select="'Video FLV'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/mpeg'">
				<xsl:value-of select="'Video MPEG'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/x-ms-asf'">
				<xsl:value-of select="'Video ASF'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/avi'">
				<xsl:value-of select="'Video AVI'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/ogv'">
				<xsl:value-of select="'Video OGV'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.format.video/mp4'">
				<xsl:value-of select="'Video MP4'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='technical.size'">
			</xsl:when>
			<xsl:when test="$key='rights.copyrightAndOtherRestrictions.yes'">
				<xsl:value-of select="'Non libre'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='rights.copyrightAndOtherRestrictions.no'">
				<xsl:value-of select="'Libre'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="$key='rights.copyrightAndOtherRestrictions.unknown'">
				<xsl:value-of select="'Licence inconnue'"></xsl:value-of>
			</xsl:when>

			<xsl:when test="$key='rights.cost.yes'">
				<xsl:value-of select="'Payant'"></xsl:value-of>

			</xsl:when>

			<xsl:when test="$key='rights.cost.no'">
				<xsl:value-of select="'Gratuit'"></xsl:value-of>

			</xsl:when>
			<xsl:when test="$key='rights.cost.unknown'">
				<xsl:value-of select="'Coût non précisé'"></xsl:value-of>

			</xsl:when>
		</xsl:choose>

	</xsl:template>
	<xsl:template name="geturi">
		<xsl:param name="key" />
		<xsl:variable name="iconpath">
			<xsl:choose>

				<xsl:when
					test="$key='general.scolomfr:generalResourceType.présentation multimédia'">
					<xsl:value-of
						select="'/general/scolomfr:generalResourceType/présentation
				multimédia.png
			'"></xsl:value-of>
				</xsl:when>
				<xsl:when test="$key='general.scolomfr:generalResourceType.diaporama'">

					<xsl:value-of
						select="'/general/scolomfr:generalResourceType/diaporama.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='general.scolomfr:generalResourceType.film'">

					<xsl:value-of
						select="'/general/scolomfr:generalResourceType/film.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.learningResourceType.exercise'">

					<xsl:value-of
						select="'/educational/learningresourcetype/exercise.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.learningResourceType.lecture'">

					<xsl:value-of select="'/educational/learningresourcetype/lecture.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.learningResourceType.animation'">

					<xsl:value-of
						select="'/educational/learningresourcetype/animation.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when
					test="$key='educational.learningResourceType.scénario pédagogique'">

					<xsl:value-of
						select="'/educational/learningresourcetype/scénario%20pédagogique.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.learningResourceType.guide'">

					<xsl:value-of select="'/educational/learningresourcetype/guide.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.intendedEndUserRole.teacher'">

					<xsl:value-of select="'/educational/intendedenduserrole/teacher.png
			'"></xsl:value-of>


				</xsl:when>
				<xsl:when test="$key='educational.intendedEndUserRole.learner'">

					<xsl:value-of select="'/educational/intendedenduserrole/learner.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.typicallearningtime.duration'">
					<xsl:value-of select="'/educational/typicallearningtime/duration.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.scolomfr:place.en salle de classe'">
					<xsl:value-of
						select="'/educational/scolomfr:place/en%20salle%20de%20classe.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.scolomfr:tool.TBI'">

					<xsl:value-of select="'/educational/scolomfr:tool/TBI.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='educational.scolomfr:tool.tablette informatique'">

					<xsl:value-of
						select="'/educational/scolomfr:tool/tablette%20informatique.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.text/html'">

					<xsl:value-of select="'/technical/format/text%20html.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.text/rtf'">

					<xsl:value-of select="'/technical/format/text%20rtf.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.application/vnd.ms-powerpoint'">

					<xsl:value-of
						select="'/technical/format/application%20vnd.ms-powerpoint.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.application/pdf'">

					<xsl:value-of select="'/technical/format/application%20pdf.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.application/x-uniboard+zip'">

					<xsl:value-of
						select="'/technical/format/application%20x-uniboard+zip.png
			'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.image/jpeg'">

					<xsl:value-of select="'/technical/format/image%20jpeg.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.image/gif'">

					<xsl:value-of select="'/technical/format/image%20gif.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.image/png'">

					<xsl:value-of select="'/technical/format/image%20png.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.image/bmp'">


					<xsl:value-of select="'/technical/format/image%20bmp.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.image/tiff'">


					<xsl:value-of select="'/technical/format/image%20tiff.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.image/svg'">


					<xsl:value-of select="'/technical/format/image%20svg.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.video/flv'">


					<xsl:value-of select="'/technical/format/video%20flv.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.video/mpeg'">


					<xsl:value-of select="'/technical/format/video%20mpeg.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.video/x-ms-asf'">


					<xsl:value-of select="'/technical/format/video%20x-ms-asf.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.video/avi'">


					<xsl:value-of select="'/technical/format/video%20avi.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.video/ogv'">


					<xsl:value-of select="'/technical/format/video%20ogv.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='technical.format.video/mp4'">

					<xsl:value-of select="'/technical/format/video%20mp4.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='technical.format.application/x-shockwave-flash'">
					<xsl:value-of
						select="'/technical/format/application%20x-shockwave-flash.png'"></xsl:value-of>
				</xsl:when>

				<xsl:when test="$key='technical.size'">
					<xsl:value-of select="'/technical/size.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='rights.copyrightAndOtherRestrictions.yes'">
					<xsl:value-of select="'/rights/copyrightAndOtherRestrictions/yes.png
			'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='rights.copyrightAndOtherRestrictions.no'">
					<xsl:value-of select="'/rights/copyrightAndOtherRestrictions/no.png
			'"></xsl:value-of>

				</xsl:when>



				<xsl:when test="$key='rights.copyrightAndOtherRestrictions.unknown'">
					<xsl:value-of
						select="'/rights/copyrightAndOtherRestrictions/unknown.png
			'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='rights.cost.yes'">
					<xsl:value-of select="'/rights/cost/yes.png'"></xsl:value-of>

				</xsl:when>

				<xsl:when test="$key='rights.cost.no'">
					<xsl:value-of select="'/rights/cost/no.png'"></xsl:value-of>

				</xsl:when>
				<xsl:when test="$key='rights.cost.unknown'">
					<xsl:value-of select="'/rights/cost/unknown.png'"></xsl:value-of>

				</xsl:when>

				<xsl:otherwise>
					<xsl:value-of
						select="concat('icon_key_not_found_in_xslt_function_get_uri_for_:_', $key)"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="concat($cdn, $icon-base,  normalize-space($iconpath))"></xsl:value-of>
	</xsl:template>
	<xsl:template name="getroleinuserlanguage">
		<xsl:param name="role" />
		<xsl:choose>
			<xsl:when test="contains($role,'author')">
				<xsl:value-of select="'auteur'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'graphical designer')">
				<xsl:value-of select="'concepteur graphique'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'instructional designer')">
				<xsl:value-of select="'concepteur pédagogique'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'contributor')">
				<xsl:value-of select="'contributeur'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'publisher')">
				<xsl:value-of select="'éditeur'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'subject matter expert')">
				<xsl:value-of select="'expert du domaine'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'content provider')">
				<xsl:value-of select="'fournisseur de contenu'"></xsl:value-of>
			</xsl:when>
			<xsl:when test="contains($role,'technical implementer')">
				<xsl:value-of select="'implémenteur technique'"></xsl:value-of>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$role"></xsl:value-of>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>
	<xsl:template name="extract-param-from-vcard">


		<xsl:param name="vcard" />
		<xsl:param name="paramname" />

		<xsl:variable name="vcardUP"
			select="translate($vcard,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
		<xsl:variable name="paramnameUP"
			select="translate($paramname,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />


		<xsl:variable name="subStringBeforeParam"
			select="substring-before($vcardUP, concat($paramnameUP,':'))" />
		<xsl:variable name="indexParam" select="string-length($subStringBeforeParam)" />
		<xsl:variable name="lengthParam"
			select="string-length(concat($paramnameUP,':'))" />
		<xsl:variable name="indexAfterParam" select="$indexParam+$lengthParam+1" />

		<xsl:variable name="aftername"
			select="normalize-space(substring($vcard,$indexAfterParam))" />
		<xsl:variable name="aftername0">
			<xsl:choose>
				<xsl:when test="contains($aftername, 'VERSION:3.0') ">
					<xsl:value-of select="substring-after($aftername, 'VERSION:3.0')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$aftername" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp1" select="$aftername0" />
		<xsl:variable name="aftername1">
			<xsl:choose>
				<xsl:when test="contains($temp1, 'ORG:') ">
					<xsl:value-of select="concat('&#160;',substring-before($temp1, 'ORG:'))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp1" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp2" select="$aftername1" />
		<xsl:variable name="aftername2">
			<xsl:choose>
				<xsl:when test="contains($temp2, 'org:') ">
					<xsl:value-of select="concat('&#160;',substring-before($temp2, 'org:'))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp2" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp3" select="$aftername2" />
		<xsl:variable name="aftername3">
			<xsl:choose>
				<xsl:when test="contains($temp3, 'N:')  ">
					<xsl:value-of select="concat('&#160;',substring-before($temp3, 'N:'))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp3" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp4" select="$aftername3" />
		<xsl:variable name="aftername4">
			<xsl:choose>
				<xsl:when test="contains($temp4, 'n:')  ">
					<xsl:value-of select="substring-before($temp4, 'n:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp4" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp5" select="$aftername4" />
		<xsl:variable name="aftername5">
			<xsl:choose>
				<xsl:when test="contains($temp5, 'URL:')  ">
					<xsl:value-of select="substring-before($temp5, 'URL:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp5" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp6" select="$aftername5" />
		<xsl:variable name="aftername6">
			<xsl:choose>
				<xsl:when test="contains($temp6, 'url:')  ">
					<xsl:value-of select="substring-before($temp6, 'url:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp6" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp7" select="$aftername6" />
		<xsl:variable name="aftername7">
			<xsl:choose>
				<xsl:when test="contains($temp7, 'EMAIL;') ">
					<xsl:value-of select="substring-before($temp7, 'EMAIL;')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp7" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp8" select="$aftername7" />
		<xsl:variable name="aftername8">
			<xsl:choose>
				<xsl:when test="contains($temp8, 'email;')">
					<xsl:value-of select="substring-before($temp8, 'email;')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp8" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp9" select="$aftername8" />
		<xsl:variable name="aftername9">
			<xsl:choose>
				<xsl:when test="contains($temp9, 'EMAIL:')">
					<xsl:value-of select="substring-before($temp9, 'EMAIL:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp9" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp10" select="$aftername9" />
		<xsl:variable name="aftername10">
			<xsl:choose>
				<xsl:when test="contains($temp10, 'email:')">
					<xsl:value-of select="substring-before($temp10, 'email:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp10" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp11" select="$aftername10" />
		<xsl:variable name="aftername11">
			<xsl:choose>
				<xsl:when test="contains($temp11, 'END:')">
					<xsl:value-of select="concat('&#160;',substring-before($temp11, 'END:'))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp11" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp12" select="$aftername11" />
		<xsl:variable name="aftername12">
			<xsl:choose>
				<xsl:when test="contains($temp12, 'end:')">
					<xsl:value-of select="substring-before($temp12, 'end:')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp12" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp13" select="$aftername12" />
		<xsl:variable name="aftername13">
			<xsl:choose>
				<xsl:when test="contains($temp13, '\N')">
					<xsl:value-of select="substring-before($temp13, '\N')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp13" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="temp14" select="$aftername13" />
		<xsl:variable name="aftername14">
			<xsl:choose>
				<xsl:when test="contains($temp14, '\n')">
					<xsl:value-of select="substring-before($temp14, '\n')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$temp14" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$aftername14">
			<xsl:value-of select="normalize-space($aftername14)" />
		</xsl:if>
	</xsl:template>
	<xsl:template name="string-replace-all">
		<xsl:param name="text" />
		<xsl:param name="replace" />
		<xsl:param name="by" />
		<xsl:choose>
			<xsl:when test="contains($text, $replace)">
				<xsl:value-of select="substring-before($text,$replace)" />
				<xsl:value-of select="$by" />
				<xsl:call-template name="string-replace-all">
					<xsl:with-param name="text"
						select="substring-after($text,$replace)" />
					<xsl:with-param name="replace" select="$replace" />
					<xsl:with-param name="by" select="$by" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="share-link">
		<xsl:param name="icon" />
		<xsl:param name="href" />
		<xsl:param name="label" />
		<xsl:element name="a" namespace="">
			<xsl:attribute name="title">
			<xsl:value-of select="$label"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="id">
			<xsl:value-of select="concat('share-',$label)"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="href">
			<xsl:value-of select="$href"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="rel">
			<xsl:value-of select="'nofollow'"></xsl:value-of>
			</xsl:attribute>
			<xsl:element name="img" namespace="">
				<xsl:attribute name="src">
											<xsl:value-of
					select="concat($cdn, $version,'/img/', $icon, '.png')"></xsl:value-of>
				</xsl:attribute>
				<xsl:attribute name="title">
			<xsl:value-of select="$label"></xsl:value-of>
			</xsl:attribute>
				<xsl:attribute name="alt">
			<xsl:value-of select="$label"></xsl:value-of>
			</xsl:attribute>
				<xsl:attribute name="class">
						  <xsl:value-of select="'share-logo'"></xsl:value-of>
						  </xsl:attribute>
			</xsl:element>

		</xsl:element>
	</xsl:template>
	<xsl:template name="url-encode">

		<xsl:param name="str" />
		<xsl:variable name="ascii">
			<xsl:text> !"#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~</xsl:text>
		</xsl:variable>

		<xsl:variable name="latin1">
			<xsl:value-of
				select="' ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ'"></xsl:value-of>
		</xsl:variable>
		<!-- Characters that usually don't need to be escaped -->
		<xsl:variable name="safe">
			<xsl:text>!'()*-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz:~"</xsl:text>
		</xsl:variable>

		<xsl:variable name="hex">
			0123456789ABCDEF
		</xsl:variable>

		<xsl:if test="$str">
			<xsl:variable name="first-char" select="substring($str,1,1)" />
			<xsl:choose>
				<xsl:when test="contains($safe,$first-char)">
					<xsl:value-of select="$first-char" />
				</xsl:when>
				<xsl:when test="$first-char=' '">
					<xsl:value-of select="'+'" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="codepoint">
						<xsl:choose>
							<xsl:when test="contains($ascii,$first-char)">
								<xsl:value-of
									select="string-length(substring-before($ascii,$first-char)) + 32" />
							</xsl:when>
							<xsl:when test="contains($latin1,$first-char)">
								<xsl:value-of
									select="string-length(substring-before($latin1,$first-char)) + 160" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:message terminate="no">
								</xsl:message>
								<xsl:text>63</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="hex-digit1"
						select="substring($hex,floor($codepoint div 16) + 1,1)" />
					<xsl:variable name="hex-digit2"
						select="substring($hex,$codepoint mod 16 + 1,1)" />
					<!-- <xsl:value-of select="concat('%',$hex-digit2)"/> -->
					<xsl:value-of select="concat('%',$hex-digit1,$hex-digit2)" />
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="string-length($str) &gt; 1">
				<xsl:call-template name="url-encode">
					<xsl:with-param name="str" select="substring($str,2)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
