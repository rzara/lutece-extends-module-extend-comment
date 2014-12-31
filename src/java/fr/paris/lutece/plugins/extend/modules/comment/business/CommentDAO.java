/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.comment.business;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * This class provides Data Access methods for Comment objects.
 */
public class CommentDAO implements ICommentDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_comment ) FROM extend_comment ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_comment ( id_comment, id_resource, resource_type, date_comment, name, email, ip_address, comment, is_published, date_last_modif, id_parent_comment, is_admin_comment ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_comment, id_resource, resource_type, date_comment, name, email, ip_address, comment, is_published, date_last_modif, id_parent_comment, is_admin_comment FROM extend_comment ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_comment = ? ";
    private static final String SQL_QUERY_SELECT_BY_RESOURCE = SQL_QUERY_SELECT_ALL
            + " WHERE id_resource LIKE ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT_ID_BY_RESOURCE = "SELECT id_comment FROM extend_comment WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT_NB_COMMENT_BY_RESOURCE = " SELECT count(id_comment) FROM extend_comment WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_comment WHERE id_comment = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_RESOURCE = " DELETE FROM extend_comment WHERE resource_type = ? ";
    private static final String SQL_QUERY_FILTER_ID_RESOURCE = " AND id_resource = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_comment SET id_resource = ?, resource_type = ?, date_comment = ?, name = ?, email = ?, "
            + " ip_address = ?, comment = ?, is_published = ?, date_last_modif = ?, id_parent_comment = ?, is_admin_comment = ? WHERE id_comment = ?  ";
    private static final String SQL_QUERY_FIND_BY_ID_PARENT = SQL_QUERY_SELECT_ALL + " WHERE id_parent_comment = ? ";
    private static final String SQL_QUERY_COUNT_BY_ID_PARENT = " SELECT count( id_comment ) FROM extend_comment WHERE id_parent_comment = ? ";
    private static final String SQL_QUERY_UPDATE_COMMENT_PUBLISHED = " UPDATE extend_comment SET is_published = ?, date_last_modif = ? WHERE id_comment = ?  ";
    private static final String SQL_QUERY_SELECT_DISTINCT_ID_RESOURCES = " SELECT DISTINCT(id_resource) FROM extend_comment e WHERE resource_type = ? ";
    private static final String SQL_ORDER_BY_DATE_MODIFICATION = " ORDER BY date_last_modif ";
    private static final String SQL_COUNT_NUMBER_COMMENTS_FOR_SELECT_ID_RESOURCE = " SELECT COUNT( id_resource ) FROM extend_comment ec WHERE e.id_resource = ec.id_resource AND e.resource_type = ec.resource_type ";
    private static final String SQL_FILTER_STATUS_PUBLISHED = " is_published = 1 ";
    private static final String SQL_FILTER_SELECT_PARENTS = " id_parent_comment = 0 ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_ASC = " ASC ";
    private static final String SQL_DESC = " DESC ";
    private static final String SQL_LIMIT = " LIMIT ";
    private static final String SQL_ORDER_BY = " ORDER BY ";

    private static final String SQL_SORT_BY_DATE_CREATION = "date_comment";
    private static final String SQL_SORT_BY_DATE_MODIFICATION = "date_last_modif";

    private static final String CONSTANT_COMMA = ",";
    private static final String CONSTANT_QUESTION_MARK = "?";
    private static final String CONSTANT_OPEN_PARENTHESIS = " ( ";
    private static final String CONSTANT_CLOSE_PARENTHESIS = " ) ";
    private static final String CONSTANT_ALL_RESSOURCE_ID = "*";
    private static final String CONSTANT_SQL_ALL_RESSOURCE_ID = "%";

    /**
     * Generates a new primary key.
     * 
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Comment comment, Plugin plugin )
    {
        int nNewPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        comment.setIdComment( nNewPrimaryKey );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, comment.getIdComment( ) );
        daoUtil.setString( nIndex++, comment.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, comment.getExtendableResourceType( ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateComment( ) );
        daoUtil.setString( nIndex++, comment.getName( ) );
        daoUtil.setString( nIndex++, comment.getEmail( ) );
        daoUtil.setString( nIndex++, comment.getIpAddress( ) );
        daoUtil.setString( nIndex++, comment.getComment( ) );
        daoUtil.setBoolean( nIndex++, comment.isPublished( ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateLastModif( ) );
        daoUtil.setInt( nIndex++, comment.getIdParentComment( ) );
        daoUtil.setBoolean( nIndex, comment.getIsAdminComment( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment load( int nIdComment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdComment );
        daoUtil.executeQuery( );

        Comment comment = null;

        if ( daoUtil.next( ) )
        {
            comment = getCommentInfo( daoUtil );
        }

        daoUtil.free( );

        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdComment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdComment );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        int nIndex = 1;
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_BY_ID_RESOURCE );
        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            sbSql.append( SQL_QUERY_FILTER_ID_RESOURCE );
        }
        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        daoUtil.setString( nIndex++, strExtendableResourceType );
        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            daoUtil.setString( nIndex, strIdExtendableResource );
        }

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Comment comment, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, comment.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, comment.getExtendableResourceType( ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateComment( ) );
        daoUtil.setString( nIndex++, comment.getName( ) );
        daoUtil.setString( nIndex++, comment.getEmail( ) );
        daoUtil.setString( nIndex++, comment.getIpAddress( ) );
        daoUtil.setString( nIndex++, comment.getComment( ) );
        daoUtil.setBoolean( nIndex++, comment.isPublished( ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateLastModif( ) );
        daoUtil.setInt( nIndex++, comment.getIdParentComment( ) );
        daoUtil.setBoolean( nIndex++, comment.getIsAdminComment( ) );

        daoUtil.setInt( nIndex, comment.getIdComment( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCommentStatus( int nIdComment, boolean bPublished, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_COMMENT_PUBLISHED, plugin );
        daoUtil.setBoolean( nIndex++, bPublished );
        daoUtil.setTimestamp( nIndex++, new Timestamp( new Date( ).getTime( ) ) );
        daoUtil.setInt( nIndex, nIdComment );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentNb( String strIdExtendableResource, String strExtendableResourceType, boolean bParentsOnly,
            boolean bPublishedOnly, Plugin plugin )
    {
        int nIndex = 1;
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_NB_COMMENT_BY_RESOURCE );

        if ( bPublishedOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
        if ( bParentsOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_SELECT_PARENTS );
        }
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );

        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery( );

        int nCount = 0;

        if ( daoUtil.next( ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> selectLastComments( String strIdExtendableResource, String strExtendableResourceType,
            int nNbComments, boolean bPublishedOnly, boolean bParentsOnly, Plugin plugin )
    {
        List<Comment> listComments = new ArrayList<Comment>( );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_BY_RESOURCE );

        if ( bPublishedOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
        if ( bParentsOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_SELECT_PARENTS );
        }

        sbSQL.append( SQL_ORDER_BY_DATE_MODIFICATION ).append( SQL_DESC );
        sbSQL.append( SQL_LIMIT ).append( nNbComments );

        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listComments.add( getCommentInfo( daoUtil ) );
        }

        daoUtil.free( );

        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findParentCommentsByResource( String strIdExtendableResource,
            String strExtendableResourceType, boolean bPublishedOnly, String strSortedAttributeName, boolean bAscSort,
            int nItemsOffset, int nMaxItemsNumber, Plugin plugin )
    {
        List<Comment> listComments;
        if ( nMaxItemsNumber > 0 )
        {
            listComments = new ArrayList<Comment>( nMaxItemsNumber );
        }
        else
        {
            listComments = new ArrayList<Comment>( );
        }
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_BY_RESOURCE );
        // We only get parents
        sbSQL.append( SQL_AND ).append( SQL_FILTER_SELECT_PARENTS );
        if ( bPublishedOnly )
        {
            // We remove non published comments
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
        // We sort results
        if ( StringUtils.isNotEmpty( strSortedAttributeName ) )
        {
            if ( StringUtils.equals( SQL_SORT_BY_DATE_CREATION, strSortedAttributeName )
                    || StringUtils.equals( SQL_SORT_BY_DATE_MODIFICATION, strSortedAttributeName ) )
            {
                sbSQL.append( SQL_ORDER_BY ).append( strSortedAttributeName );
            }
            else
            {
                sbSQL.append( SQL_ORDER_BY_DATE_MODIFICATION );
            }
        }
        else
        {
            sbSQL.append( SQL_ORDER_BY_DATE_MODIFICATION );
        }
        String strSortOrder;
        if ( bAscSort )
        {
            strSortOrder = SQL_ASC;
        }
        else
        {
            strSortOrder = SQL_DESC;
        }
        sbSQL.append( strSortOrder );

        // We paginate results
        if ( nMaxItemsNumber > 0 )
        {
            sbSQL.append( SQL_LIMIT );
            if ( nItemsOffset > 0 )
            {
                sbSQL.append( CONSTANT_QUESTION_MARK ).append( CONSTANT_COMMA );
            }
            sbSQL.append( CONSTANT_QUESTION_MARK );
        }

        // We now proceed the SQL request
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        if (strIdExtendableResource.equals(CONSTANT_ALL_RESSOURCE_ID))
        {
        	daoUtil.setString( nIndex++, CONSTANT_SQL_ALL_RESSOURCE_ID );

        }
        else
        {
        	daoUtil.setString( nIndex++, strIdExtendableResource );
        }
        daoUtil.setString( nIndex++, strExtendableResourceType );
        if ( nMaxItemsNumber > 0 )
        {
            if ( nItemsOffset > 0 )
            {
                daoUtil.setInt( nIndex++, nItemsOffset );
            }
            daoUtil.setInt( nIndex++, nMaxItemsNumber );
        }
        daoUtil.executeQuery( );

        // We fetch results
        while ( daoUtil.next( ) )
        {
            listComments.add( getCommentInfo( daoUtil ) );
        }

        daoUtil.free( );

        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByIdParent( int nIdParent, boolean bPublishedOnly, String strSortedAttributeName,
            boolean bAscSort, Plugin plugin )
    {
        List<Comment> listComments = new ArrayList<Comment>( );
        int nIndex = 1;
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_FIND_BY_ID_PARENT );
        if ( bPublishedOnly )
        {
            // We remove non published comments
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
        // We sort results
        if ( StringUtils.isNotEmpty( strSortedAttributeName ) )
        {
            if ( StringUtils.equals( SQL_SORT_BY_DATE_CREATION, strSortedAttributeName )
                    || StringUtils.equals( SQL_SORT_BY_DATE_MODIFICATION, strSortedAttributeName ) )
            {
                sbSQL.append( SQL_ORDER_BY ).append( strSortedAttributeName );
            }
            else
            {
                sbSQL.append( SQL_ORDER_BY_DATE_MODIFICATION );
            }
        }
        else
        {
            sbSQL.append( SQL_ORDER_BY_DATE_MODIFICATION );
        }
        if ( bAscSort )
        {
            sbSQL.append( SQL_ASC );
        }
        else
        {
            sbSQL.append( SQL_DESC );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setInt( nIndex, nIdParent );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listComments.add( getCommentInfo( daoUtil ) );
        }
        daoUtil.free( );
        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdParent( int nIdParent, boolean bPublishedOnly, Plugin plugin )
    {
        int nIndex = 1;
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_COUNT_BY_ID_PARENT );
        if ( bPublishedOnly )
        {
            // We remove non published comments
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setInt( nIndex, nIdParent );
        daoUtil.executeQuery( );

        int nResult = 0;
        if ( daoUtil.next( ) )
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdsByResource( String strIdExtendableResource, String strExtendableResourceType,
            boolean bPublishedOnly, Plugin plugin )
    {
        List<Integer> listResult = new ArrayList<Integer>( );

        int nIndex = 1;
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ID_BY_RESOURCE );
        if ( bPublishedOnly )
        {
            // We remove non published comments
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIdComment = daoUtil.getInt( 1 );
            listResult.add( nIdComment );
        }

        daoUtil.free( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdMostCommentedResources( String strExtendableResourceType, boolean bPublishedOnly,
            int nItemsOffset, int nMaxItemsNumber, Plugin plugin )
    {
        List<Integer> listIds;
        if ( nMaxItemsNumber > 0 )
        {
            listIds = new ArrayList<Integer>( nMaxItemsNumber );
        }
        else
        {
            listIds = new ArrayList<Integer>( );
        }

        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_DISTINCT_ID_RESOURCES );
        sbSQL.append( SQL_ORDER_BY ).append( CONSTANT_OPEN_PARENTHESIS );
        sbSQL.append( SQL_COUNT_NUMBER_COMMENTS_FOR_SELECT_ID_RESOURCE );
        if ( bPublishedOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
        sbSQL.append( CONSTANT_CLOSE_PARENTHESIS );
        if ( nMaxItemsNumber > 0 )
        {
            sbSQL.append( SQL_LIMIT );
            if ( nItemsOffset > 0 )
            {
                sbSQL.append( CONSTANT_QUESTION_MARK ).append( CONSTANT_COMMA );
            }
            sbSQL.append( CONSTANT_QUESTION_MARK );
        }
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setString( nIndex++, strExtendableResourceType );
        if ( nMaxItemsNumber > 0 )
        {
            if ( nItemsOffset > 0 )
            {
                daoUtil.setInt( nIndex++, nItemsOffset );
            }
            daoUtil.setInt( nIndex, nMaxItemsNumber );
        }
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listIds;
    }

    /**
     * Fetch the attributes of a comment from a daoUtil.
     * @param daoUtil The daoUtil to get the attributes from
     * @return The comment with the attributes contained in the daoUtil.
     */
    private Comment getCommentInfo( DAOUtil daoUtil )
    {
        int nIndex = 1;
        Comment comment = new Comment( );
        comment.setIdComment( daoUtil.getInt( nIndex++ ) );
        comment.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
        comment.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
        comment.setDateComment( daoUtil.getTimestamp( nIndex++ ) );
        comment.setName( daoUtil.getString( nIndex++ ) );
        comment.setEmail( daoUtil.getString( nIndex++ ) );
        comment.setIpAddress( daoUtil.getString( nIndex++ ) );
        comment.setComment( daoUtil.getString( nIndex++ ) );
        comment.setPublished( daoUtil.getBoolean( nIndex++ ) );
        comment.setDateLastModif( daoUtil.getTimestamp( nIndex++ ) );
        comment.setIdParentComment( daoUtil.getInt( nIndex++ ) );
        comment.setIsAdminComment( daoUtil.getBoolean( nIndex++ ) );
        return comment;
    }

}
