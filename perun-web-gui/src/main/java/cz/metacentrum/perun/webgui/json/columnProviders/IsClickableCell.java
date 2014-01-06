package cz.metacentrum.perun.webgui.json.columnProviders;

/**
 * Determine if cell should be clickable or not by authz
 *
 * @author Pavel Zlamal <256627@mail.muni.cz>
 * @version $Id: 2d3d5768c7fc4334816d5ca081657a18a7e675a8 $
 */
public interface IsClickableCell<T> {

    /**
     * Return TRUE if cell should be drawn as clickable
     * based on privileges over the base object
     *
     * @return TRUE = clickable / False = draw as simple text
     */
    public boolean isClickable(T object);

    /**
     * Return destination link if set or empty string
     *
     * @return destination link or empty string
     */
    public String linkUrl(T object);

}