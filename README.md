# `frosty`
> reactive jamepad extension

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a141c62a0d074580a10974a5ece29348)](https://www.codacy.com/app/delta-leonis/frosty?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=delta-leonis/yagl4j&amp;utm_campaign=Badge_Grade)
[![CircleCI](https://circleci.com/gh/delta-leonis/algieba.svg?style=svg)](https://circleci.com/gh/delta-leonis/algieba)

You'll need at least Java 1.8 ([jre](https://www.java.com/download/)
/[jdk](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html))
to run `frosty`.

## Dependency

#### Maven

```
<dependency>
    <groupId>io.leonis</groupId>
    <artifactId>frosty</artifactId>
    <version>0.0.1</version>
</dependency>
```

#### Gradle

```
compile 'io.leonis:frosty:0.0.1'
```

### Suppliers

Since the data evolves as it is processed the type of the data container may change as well. In
order to allow composition of these data containers over functions, these functions should be
parametrized using interface instead of class types. Unfortunately Java's `Supplier` can only be
implemented once due to type erasure; this is why most data types in `frosty` have a nested
`Supplier` (or `SetSupplier`, `MappingSupplier`, e.a.) interface. This also allows for unnecessary
coupling as an function that which uses a dpad can work with any controller with a dpad.

## Documentation

The javadoc for the current code on `master` can be found on https://delta-leonis.github.io/frosty/

## Building

Make sure you have `gradle>=v2.10` installed. Run the following to build the application:

```
  gradle build
```

## Copyright

This project is licensed under the AGPL version 3 license (see LICENSE).

```
frosty - delta-leonis
Copyright (C) 2018 Jeroen de Jong, Rimon Oz

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
