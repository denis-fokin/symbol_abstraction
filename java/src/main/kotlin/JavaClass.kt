import org.example.*

/*
Maybe structure from here: https://github.com/UnitTestBot/jcdb/blob/type-system-design/design.md
*/

abstract class JavaType: AtomSymbol

abstract class JavaPrimitiveType: JavaType()

abstract class JavaRefType: JavaType()

abstract class JavaClass: JavaRefType(), SymbolWithMembers, ConstructableSymbol
/*
  - members: methods + fields
  - jClass
  - isPublic
  - isProtected
  - isPrivate
  - ...
 */

abstract class JavaTypedMethod: SymbolMember
abstract class JavaTypedField: SymbolMember