<?xml version="1.0" encoding="UTF-8"?>

<xslt:stylesheet version="1.0"
  xmlns:xslt="http://www.w3.org/1999/XSL/Transform">


<xslt:template match="interface">
  <html>
    <body>
      <h1>IServiceREST</h1>

         <xslt:for-each select="include">
        <h3>
          <xslt:value-of select="text()"/>
        </h3>
      </xslt:for-each>

      <table border="1">
        <!-- <tr bgcolor = "#3558ab"> Note - Remember this color -->
        <tr bgcolor = "#958d8d">
          <th>
            Method
          </th>
          <th>
            Return type
          </th>
          <th>
            Input parameters
          </th>
        </tr>

        <xslt:for-each select="abstract_method">
          <tr>
            <td>
              <xslt:value-of select="@id"/>
            </td>
            <td>
              <xslt:value-of select="return"/>
            </td>
            <td>

  <xslt:for-each select="parameterList">
                <xslt:apply-templates select="parameter"/>
 </xslt:for-each>

            </td>
          </tr>
        </xslt:for-each>

      </table>
    </body>
  </html>

</xslt:template>

<xslt:template match="parameter">
  <xslt:value-of select="."/>:
 <xslt:value-of select="@type"/>
<xslt:if test="position() != last()">,
  </xslt:if>


</xslt:template>
</xslt:stylesheet>
