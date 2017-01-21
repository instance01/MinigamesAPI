# Minigames-Lib 2.0 - Development

## The gaming rules

A minigame is built on top of small gaming rules. Those gaming rules influence the behavior of the minigame as well as
the mechanic.

Under the hood the gaming rules are divided into two pieces:
* predicates listening on spigot events
* action handlers to do something

The predicates are helper functions (java 8 lamdas) that are queried once an event is detected.

### Execution order of events

The predicates of a rule are typically executed in following order:

* global/core predicate
* arena related predicate (inherited from arena types)
* arena state related predicate
* match related predicate
* match phase related predicate
* player state related predicate
* player related predicate

This is the default ordering of predicates. However if you add predicates to a rule you can set a numeric value (priority).
The lower this value is the later the predicate will be invoked.

In boolean logic this seems to be senseless. Let us look at the following sample:

    if (predicateA && predicateB) action;
    
    if (predicateB && predicateA) action;
    
If you think of predicates for boolean values it is senseless to order them. But predicates in minigames lib are more than
just boolean values or lambdas returning booleans.

Minigame predicates can return the following states:

* TRUE (-> test the next predicate of this rule in order)
* FALSE (-> abort the execution)
* FORCE (-> skip all other predicates)

So it is important for us to have some ordering in minigame predicates.

### Type of fetched events

Rules can manage all relevant bukkit events and minigame events.

### Common predicates

TODO

### Common actions

TODO

### Common rules

TODO

### Common rule sets

TODO

### Common arena types

TODO

## Configurable rules

TODO

## Votable rules

TODO
