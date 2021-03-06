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

import fr.paris.lutece.plugins.avatar.service.AvatarService;
import fr.paris.lutece.plugins.extend.modules.comment.util.annotation.Email;
import fr.paris.lutece.portal.service.editor.EditorBbcodeService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;


/**
 * 
 * Comment
 * 
 */
public class Comment
{
    private int _nIdComment;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private Timestamp _dateComment;
    private Timestamp _dateLastModif;
    private String _strName;
    @Email
    private String _strEmail;
    private String _strIpAddress;
    private String _strComment;
    private boolean _bIsPublished;
    private int _nIdParentComment;
    private List<Comment> _listSubComments;
    private int _nNumberSubComments = -1;
    private boolean _bIsAdminComment;

    /**
     * @return the strIdExtendableResource
     */
    public String getIdExtendableResource( )
    {
        return _strIdExtendableResource;
    }

    /**
     * @param strIdExtendableResource the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * @return the extendableResourceType
     */
    public String getExtendableResourceType( )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * Returns the date of the comment
     * @return the date of the comment
     */
    public Timestamp getDateComment( )
    {
        return _dateComment;
    }

    /**
     * Sets the date of the comment
     * @param dateComment the new date
     */
    public void setDateComment( Timestamp dateComment )
    {
        _dateComment = dateComment;
    }

    /**
     * Get the date of last modification of the comment
     * @return The date of last modification of the comment
     */
    public Timestamp getDateLastModif( )
    {
        return _dateLastModif;
    }

    /**
     * Set the date of last modification of the comment
     * @param dateLastModif The date of last modification of the comment
     */
    public void setDateLastModif( Timestamp dateLastModif )
    {
        this._dateLastModif = dateLastModif;
    }

    /**
     * Returns the identifier of the object
     * @return The identifier of the object
     */
    public int getIdComment( )
    {
        return _nIdComment;
    }

    /**
     * Sets the identifier of the object
     * @param nIdComment the new identifier
     */
    public void setIdComment( int nIdComment )
    {
        _nIdComment = nIdComment;
    }

    /**
     * Gets the checks if is published.
     * 
     * @return the checks if is published
     */
    public boolean isPublished( )
    {
        return _bIsPublished;
    }

    /**
     * Sets the published.
     * 
     * @param bIsPublished the new published
     */
    public void setPublished( boolean bIsPublished )
    {
        _bIsPublished = bIsPublished;
    }

    /**
     * Returns the comment
     * @return the comment
     */
    public String getComment( )
    {
        return _strComment;
    }

    /**
     * Sets the comment
     * @param strComment the new comment
     */
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }

    /**
     * Get the content of the comment interpreted as BBCode
     * @return The content of the comment interpreted as BBCode
     */
    public String getBBCodeComment( )
    {
        try
        {
            return EditorBbcodeService.getInstance( ).parse( _strComment );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        return _strComment;
    }

    /**
     * Returns the email of the user
     * @return the email of the user
     */
    public String getEmail( )
    {
        return _strEmail;
    }

    /**
     * Sets the email of the user
     * @param strEmail the new email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Returns the IP address of the user
     * @return the IP address of the user
     */
    public String getIpAddress( )
    {
        return _strIpAddress;
    }

    /**
     * Sets the IP address of the user
     * @param strIpAddress the new IP address
     */
    public void setIpAddress( String strIpAddress )
    {
        _strIpAddress = strIpAddress;
    }

    /**
     * Returns the name of the user
     * @return the name of the user
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the name of the user
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Get the id of the parent comment of this comment
     * @return The id of the parent comment of this comment
     */
    public int getIdParentComment( )
    {
        return _nIdParentComment;
    }

    /**
     * Set the id of the parent comment of this comment
     * @param nIdParentComment The id of the parent comment of this comment
     */
    public void setIdParentComment( int nIdParentComment )
    {
        this._nIdParentComment = nIdParentComment;
    }

    /**
     * Get the list of sub comments of this comment
     * @return The list of sub comments of this comment. An empty list means
     *         that this comment has no sub comment. A null value means that the
     *         sub comment list has not been initialized.
     */
    public List<Comment> getListSubComments( )
    {
        return _listSubComments;
    }

    /**
     * Set the list of sub comments of this comment, and update the number of
     * sub comments.
     * @param listSubComments The list of sub comments of this comment.
     */
    public void setListSubComments( List<Comment> listSubComments )
    {
        this._listSubComments = listSubComments;
        if ( listSubComments != null )
        {
            this._nNumberSubComments = listSubComments.size( );
        }
    }

    /**
     * Get the number of sub comments of this comment
     * @return the number of sub comments of this comment, or -1 if this
     *         information is not known.
     */
    public int getNumberSubComments( )
    {
        return _nNumberSubComments;
    }

    /**
     * Set the number of sub comments of this comment
     * @param nNumberSubComments the number of sub comments of this comment, or
     *            -1 if this information is not known.
     */
    public void setNumberSubComments( int nNumberSubComments )
    {
        this._nNumberSubComments = nNumberSubComments;
    }

    /**
     * Check if the comment has been written by an admin or by a front office
     * user
     * @return True if the comment has been written by an admin, false otherwise
     */
    public boolean getIsAdminComment( )
    {
        return _bIsAdminComment;
    }

    /**
     * Set the admin written status of this comment
     * @param bIsAdminComment True if the comment has been written by an admin,
     *            false otherwise
     */
    public void setIsAdminComment( boolean bIsAdminComment )
    {
        this._bIsAdminComment = bIsAdminComment;
    }

    /**
     * Get the id of the first unpublished sub comment of this comment
     * @return The id of the first unpublished sub comment, or 0 if it has no
     *         sub comments or no unpublished sub comments. 0 is also returned
     *         if sub comments of this comment have not been fetched.
     */
    public int getFirstUnpublishedSubComment( )
    {
        if ( _listSubComments == null || _listSubComments.size( ) == 0 )
        {
            return 0;
        }
        for ( Comment comment : _listSubComments )
        {
            if ( !comment.isPublished( ) )
            {
                return comment.getIdComment( );
            }
        }
        return 0;
    }
    
    /**
     * Display the avatar of the comment's author
     * @return The HTML code of the avatar
     */
    public String getAvatar()
    {
        return AvatarService.getAvatar( _strEmail );
    }

    /**
     * Display the avatar of the comment's author
     * @return The HTML code of the avatar
     */
    public String getAvatarUrl()
    {
        return AvatarService.getAvatarUrl( _strEmail );
    }
}
