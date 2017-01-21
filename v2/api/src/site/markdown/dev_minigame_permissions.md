# Minigames-Lib 2.0 - Development

## Permissions

Every permission in minigames lib is declared within enumerations.

## Declaring a permission

To declare a permission simply create an enumeration.

    /**
     * My plugin permissions.
     *
     * @author mepeisen
     */
    @Permissions("$PERM:MINIGAME:myplugin$.common")
    public enum MyPermissions implements PermissionsInterface
    {
    
        /**
         * Some permission.
         */
        @Permission
        Something,
        
    }
    
Your plugin provider now simply needs to return this class that minigames lib will do the rest.

    @Override
    public Iterable<Class<? extends Enum<?>>> getPermissions()
    {
        final List<Class<? extends Enum<?>>> result = new ArrayList<>();
        result.add(MyPermissions.class);
        return result;
    }
    
Hint: The string "$MINIGAME:myplugin$.common" contains a variable substitution. Permissions in minigames lib
should always use this substitution because administrators can individually configure the path of minigame
permissions.

The sub paths "core" is always preserved for minigames lib itself or for arenas configurations. It comes
from permission sets (explained later on). You should not use this path because on newer versions of the
minigames lib some configuration option may be added and you will get into trouble.

## String substitution

The CommonPermissions enumeration in the API uses the following annotation:

    @Permissions("$PERM:MGLIB$")
    
As you can see, the path is somehow cryptic. It contains a so called variable substitution.

Details on variable substitution are explained in chapter [Variable substitution](dev_advanced_variable_substitution.html).

## Permission name

The permission name is built from @Permissions annotation and the enumeration name.
You can override the enumeration name by giving the @Permission annotation a value.

After calculating the permission name the strings will be substituted.

## Using the permission

Using the permission is fairly simple:

    somePlayer.checkPermission(MyPermissions.Something)

## Special permissions type: Permission sets

The configuration sets are predefined configuration options used within typical minigames.

TODO

## Revisions and migration

TODO
