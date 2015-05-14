<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:atom="http://www.w3.org/2005/Atom" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:apiscol="http://www.crdp.ac-versailles.fr/2012/apiscol"
	exclude-result-prefixes="#default">
	<xsl:output method="html" omit-xml-declaration="yes"
		encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<meta charset="utf-8">
				</meta>


				<style>
					/* ------------------
					styling for the tables
					------------------ */

					div.apiscol-list table#hor-minimalist-a thead tr th img {
					vertical-align: bottom;
					display: inline-block;
					margin-right: 1em;
					}

					#hor-minimalist-a {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					background: #fff;
					margin: 45px;
					border-collapse: collapse;
					text-align: left;
					vertical-align: top;
					width: 1000px;
					table-layout: fixed;
					}

					#hor-minimalist-a tr,#hor-minimalist-a td {
					vertical-align: top;
					}

					#hor-minimalist-a tbody tr td, #hor-minimalist-a tbody tr div{
					height: 2.5em;
					max-height: 2.5em;
					overflow-y: hidden;
					}
					#hor-minimalist-a tbody tr div{
					margin: 0;
					padding: 0;
					}

					#hor-minimalist-a tbody tr.bottom {
					border-bottom: thin solid #6678b1;
					padding-bottom: 0.5em;
					}

					#hor-minimalist-a th {
					font-size: 14px;
					font-weight: normal;
					color: #039;
					padding: 10px 8px;
					border-bottom: 2px solid #6678b1;
					}

					#hor-minimalist-a td {
					color: #669;
					padding: 9px 8px 0px 8px;
					}

					#hor-minimalist-a tbody tr:hover td {
					color: #009;
					}

					#hor-minimalist-b {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					background: #fff;
					margin: 45px;
					width: 480px;
					border-collapse: collapse;
					text-align: left;
					}

					#hor-minimalist-b th {
					font-size: 14px;
					font-weight: normal;
					color: #039;
					padding: 10px 8px;
					border-bottom: 2px solid #6678b1;
					}

					#hor-minimalist-b td {
					border-bottom: 1px solid #ccc;
					color: #669;
					padding: 6px 8px;
					}

					#hor-minimalist-b tbody tr:hover td {
					color: #009;
					}

					#ver-minimalist {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#ver-minimalist th {
					padding: 8px 2px;
					font-weight: normal;
					font-size: 14px;
					border-bottom: 2px solid #6678b1;
					border-right: 30px solid #fff;
					border-left: 30px solid #fff;
					color: #039;
					}

					#ver-minimalist td {
					padding: 12px 2px 0px 2px;
					border-right: 30px solid #fff;
					border-left: 30px solid #fff;
					color: #669;
					}

					#box-table-a {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#box-table-a th {
					font-size: 13px;
					font-weight: normal;
					padding: 8px;
					background: #b9c9fe;
					border-top: 4px solid #aabcfe;
					border-bottom: 1px solid #fff;
					color: #039;
					}

					#box-table-a td {
					padding: 8px;
					background: #e8edff;
					border-bottom: 1px solid #fff;
					color: #669;
					border-top: 1px solid transparent;
					}

					#box-table-a tr:hover td {
					background: #d0dafd;
					color: #339;
					}

					#box-table-b {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: center;
					border-collapse: collapse;
					border-top: 7px solid #9baff1;
					border-bottom: 7px solid #9baff1;
					}

					#box-table-b th {
					font-size: 13px;
					font-weight: normal;
					padding: 8px;
					background: #e8edff;
					border-right: 1px solid #9baff1;
					border-left: 1px solid #9baff1;
					color: #039;
					}

					#box-table-b td {
					padding: 8px;
					background: #e8edff;
					border-right: 1px solid #aabcfe;
					border-left: 1px solid #aabcfe;
					color: #669;
					}

					#hor-zebra {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#hor-zebra th {
					font-size: 14px;
					font-weight: normal;
					padding: 10px 8px;
					color: #039;
					}

					#hor-zebra td {
					padding: 8px;
					color: #669;
					}

					#hor-zebra .odd {
					background: #e8edff;
					}

					#ver-zebra {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#ver-zebra th {
					font-size: 14px;
					font-weight: normal;
					padding: 12px 15px;
					border-right: 1px solid #fff;
					border-left: 1px solid #fff;
					color: #039;
					}

					#ver-zebra td {
					padding: 8px 15px;
					border-right: 1px solid #fff;
					border-left: 1px solid #fff;
					color: #669;
					}

					.vzebra-odd {
					background: #eff2ff;
					}

					.vzebra-even {
					background: #e8edff;
					}

					#ver-zebra #vzebra-adventure,#ver-zebra #vzebra-children {
					background: #d0dafd;
					border-bottom: 1px solid #c8d4fd;
					}

					#ver-zebra #vzebra-comedy,#ver-zebra #vzebra-action {
					background: #dce4ff;
					border-bottom: 1px solid #d6dfff;
					}

					#one-column-emphasis {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#one-column-emphasis th {
					font-size: 14px;
					font-weight: normal;
					padding: 12px 15px;
					color: #039;
					}

					#one-column-emphasis td {
					padding: 10px 15px;
					color: #669;
					border-top: 1px solid #e8edff;
					}

					.oce-first {
					background: #d0dafd;
					border-right: 10px solid transparent;
					border-left: 10px solid transparent;
					}

					#one-column-emphasis tr:hover td {
					color: #339;
					background: #eff2ff;
					}

					#newspaper-a {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					border: 1px solid #69c;
					}

					#newspaper-a th {
					padding: 12px 17px 12px 17px;
					font-weight: normal;
					font-size: 14px;
					color: #039;
					border-bottom: 1px dashed #69c;
					}

					#newspaper-a td {
					padding: 7px 17px 7px 17px;
					color: #669;
					}

					#newspaper-a tbody tr:hover td {
					color: #339;
					background: #d0dafd;
					}

					#newspaper-b {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					border: 1px solid #69c;
					}

					#newspaper-b th {
					padding: 15px 10px 10px 10px;
					font-weight: normal;
					font-size: 14px;
					color: #039;
					}

					#newspaper-b tbody {
					background: #e8edff;
					}

					#newspaper-b td {
					padding: 10px;
					color: #669;
					border-top: 1px dashed #fff;
					}

					#newspaper-b tbody tr:hover td {
					color: #339;
					background: #d0dafd;
					}

					#newspaper-c {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					border: 1px solid #6cf;
					}

					#newspaper-c th {
					padding: 20px;
					font-weight: normal;
					font-size: 13px;
					color: #039;
					text-transform: uppercase;
					border-right: 1px solid #0865c2;
					border-top: 1px solid #0865c2;
					border-left: 1px solid #0865c2;
					border-bottom: 1px solid #fff;
					}

					#newspaper-c td {
					padding: 10px 20px;
					color: #669;
					border-right: 1px dashed #6cf;
					}

					#rounded-corner {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#rounded-corner thead th.rounded-company {
					background: #b9c9fe url('table-images/left.png') left -1px no-repeat;
					}

					#rounded-corner thead th.rounded-q4 {
					background: #b9c9fe url('table-images/right.png') right -1px no-repeat;
					}

					#rounded-corner th {
					padding: 8px;
					font-weight: normal;
					font-size: 13px;
					color: #039;
					background: #b9c9fe;
					}

					#rounded-corner td {
					padding: 8px;
					background: #e8edff;
					border-top: 1px solid #fff;
					color: #669;
					}

					#rounded-corner tfoot td.rounded-foot-left {
					background: #e8edff url('table-images/botleft.png') left bottom
					no-repeat;
					}

					#rounded-corner tfoot td.rounded-foot-right {
					background: #e8edff url('table-images/botright.png') right bottom
					no-repeat;
					}

					#rounded-corner tbody tr:hover td {
					background: #d0dafd;
					}

					#background-image {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					background: url('table-images/blurry.jpg') 330px 59px no-repeat;
					}

					#background-image th {
					padding: 12px;
					font-weight: normal;
					font-size: 14px;
					color: #339;
					}

					#background-image td {
					padding: 9px 12px;
					color: #669;
					border-top: 1px solid #fff;
					}

					#background-image tfoot td {
					font-size: 11px;
					}

					#background-image tbody td {
					background: url('table-images/back.png');
					}

					* html #background-image tbody td { /*
					----------------------------
					PUT THIS ON IE6 ONLY STYLE
					AS THE RULE INVALIDATES
					YOUR STYLESHEET
					----------------------------
					*/
					filter:
					progid:DXImageTransform.Microsoft.AlphaImageLoader(src='table-images/back.png',
					sizingMethod='crop' );
					background: none;
					}

					#background-image tbody tr:hover td {
					color: #339;
					background: none;
					}

					#gradient-style {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					}

					#gradient-style th {
					font-size: 13px;
					font-weight: normal;
					padding: 8px;
					background: #b9c9fe url('table-images/gradhead.png') repeat-x;
					border-top: 2px solid #d3ddff;
					border-bottom: 1px solid #fff;
					color: #039;
					}

					#gradient-style td {
					padding: 8px;
					border-bottom: 1px solid #fff;
					color: #669;
					border-top: 1px solid #fff;
					background: #e8edff url('table-images/gradback.png') repeat-x;
					}

					#gradient-style tfoot tr td {
					background: #e8edff;
					font-size: 12px;
					color: #99c;
					}

					#gradient-style tbody tr:hover td {
					background: #d0dafd url('table-images/gradhover.png') repeat-x;
					color: #339;
					}

					#pattern-style-a {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					background: url('table-images/pattern.png');
					}

					#pattern-style-a thead tr {
					background: url('table-images/pattern-head.png');
					}

					#pattern-style-a th {
					font-size: 13px;
					font-weight: normal;
					padding: 8px;
					border-bottom: 1px solid #fff;
					color: #039;
					}

					#pattern-style-a td {
					padding: 8px;
					border-bottom: 1px solid #fff;
					color: #669;
					border-top: 1px solid transparent;
					}

					#pattern-style-a tbody tr:hover td {
					color: #339;
					background: #fff;
					}

					#pattern-style-b {
					font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
					font-size: 12px;
					margin: 45px;
					width: 480px;
					text-align: left;
					border-collapse: collapse;
					background: url('table-images/patternb.png');
					}

					#pattern-style-b thead tr {
					background: url('table-images/patternb-head.png');
					}

					#pattern-style-b th {
					font-size: 13px;
					font-weight: normal;
					padding: 8px;
					border-bottom: 1px solid #fff;
					color: #039;
					}

					#pattern-style-b td {
					padding: 8px;
					border-bottom: 1px solid #fff;
					color: #669;
					border-top: 1px solid transparent;
					}

					#pattern-style-b tbody tr:hover td {
					color: #339;
					background: #cdcdee;
					}
				</style>
				<xsl:element name="meta" namespace="">
					<xsl:attribute name="name">
						<xsl:value-of select="'description'"></xsl:value-of>
						</xsl:attribute>
					<xsl:attribute name="content">
						<xsl:value-of select="'Ressources pédagogiques'"></xsl:value-of>
						</xsl:attribute>
				</xsl:element>
				<meta name="viewport" content="width=device-width"></meta>

				<title>
					<xsl:value-of select="/atom:feed/atom:title"></xsl:value-of>
				</title>
			</head>
			<body>
				<xsl:apply-templates select="atom:feed"></xsl:apply-templates>


			</body>



		</html>

	</xsl:template>
	<xsl:template match="atom:feed">

		<div class="apiscol-list">
			<table id="hor-minimalist-a">
				<thead>
					<tr>
						<th colspan="12">
							<xsl:apply-templates select="atom:logo"></xsl:apply-templates>
							<xsl:apply-templates select="atom:generator"></xsl:apply-templates>
						</th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="*[local-name()='entry']"></xsl:apply-templates>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="12">
							<xsl:call-template name="pagination">
								<xsl:with-param name="length" select="/atom:feed/apiscol:length">
								</xsl:with-param>
								<xsl:with-param name="step" select="0">
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="atom:logo">
		<xsl:element name="img">
			<xsl:attribute name="src">
			<xsl:value-of select=".">
			</xsl:value-of>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="atom:generator">
		<xsl:element name="span">
			<xsl:value-of select=".">
			</xsl:value-of>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='entry']">
		<tr>
			<td colspan="5">
				<strong>
					<xsl:value-of select="*[local-name()='title']"></xsl:value-of>
				</strong>
			</td>

			<td colspan="7">
				<div>
					<xsl:value-of select="*[local-name()='summary']"></xsl:value-of>
				</div>
			</td>

		</tr>
		<tr class="bottom">
			<td>
				<xsl:element name="a">
					<xsl:attribute name="href">
			<xsl:value-of select="*[local-name()='link'][@type='text/html']/@href"></xsl:value-of>
			</xsl:attribute>
					HTML
				</xsl:element>
			</td>
			<td>
				<xsl:element name="a">
					<xsl:attribute name="href">
			<xsl:value-of
						select="*[local-name()='link'][@type='application/atom+xml']/@href"></xsl:value-of>&amp;desc=true
			</xsl:attribute>
					ATOM
				</xsl:element>
			</td>
			<td>
				<xsl:element name="a">
					<xsl:attribute name="href">
			<xsl:value-of
						select="*[local-name()='link'][@type='application/lom+xml']/@href"></xsl:value-of>
			</xsl:attribute>
					SCOLOMFR
				</xsl:element>
			</td>
			<td>
				<xsl:element name="a">
					<xsl:attribute name="href">
			<xsl:value-of select="*[local-name()='link'][@rel='icon']/@href"></xsl:value-of>
			</xsl:attribute>
					ICON
				</xsl:element>
			</td>
			<td>
				<xsl:element name="a">
					<xsl:attribute name="href">
			<xsl:value-of select="*[local-name()='content']/@src"></xsl:value-of>
			</xsl:attribute>
					DOWNLOAD
				</xsl:element>
			</td>
			<td colspan="7">
				<xsl:value-of select="*[local-name()='id']"></xsl:value-of>
			</td>

		</tr>
	</xsl:template>
	<xsl:template name="pagination">
		<xsl:param name="step"></xsl:param>
		<xsl:param name="length"></xsl:param>
		<xsl:element name="a">
			<xsl:attribute name="href">
		<xsl:value-of select="concat('meta?start=', $step*10, '&amp;rows=10')"></xsl:value-of>
		</xsl:attribute>
			<xsl:value-of select="$step+1"></xsl:value-of>
		</xsl:element>
		&#0160;
		<xsl:choose>
			<xsl:when test="($step+1)*10>length"></xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="pagination">
					<xsl:with-param name="length" select="$length">
					</xsl:with-param>
					<xsl:with-param name="step" select="$step+1">
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
