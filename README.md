# lein-blacklist

A Leiningen plugin to blacklist specific namespaces from being required.

Note that this is not a strict guarantee; methods exist of loading
code which will not be caught by this, (for instance, calling the
`require` function outside the `ns` clause or having a `:load` clause
inside `ns`) but it will catch conventional usage. The one exception
is using prefix segments inside a `:require` clause. Don't do that.

Note that this is only designed to catch direct `require`s and not
transitive usage.

## Usage

Put `[lein-blacklist "0.1.0"]` into the `:plugins` vector of your
project.clj, and add a `:blacklist` key to the file. This should be a
map where the keys are regexes for blacklisted namespaces and the
values are regexes that are exceptions to the blacklist.

```clj
  :blacklist {#"^bad.ns" [#"^super.legacy"]
              #"^bad.ns.extremely.bad$" []}
```

In this example, any namespaces under `bad.ns` are only allowed to be
required from under `super.legacy`, but `bad.ns.extremely.bad` may not
be required from anywhere.

Check by running:

    $ lein blacklist

This will return a nonzero exit code when violations are detected.

You would typically invoke this as part of a chain of tasks, for example:

```clj
  :aliases { "ci" ["do" "check," "test," "blacklist"]
    [...] }
```

## License

Copyright © 2017 Phil Hagelberg

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
