/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package shared;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-06-26")
public class Grafo implements org.apache.thrift.TBase<Grafo, Grafo._Fields>, java.io.Serializable, Cloneable, Comparable<Grafo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Grafo");

  private static final org.apache.thrift.protocol.TField VERTICES_FIELD_DESC = new org.apache.thrift.protocol.TField("vertices", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField ARESTAS_FIELD_DESC = new org.apache.thrift.protocol.TField("arestas", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new GrafoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new GrafoTupleSchemeFactory();

  public java.util.List<Vertice> vertices; // required
  public java.util.List<Aresta> arestas; // required
   /**
   * Metodos adicionais(Não gerados automaticamente) 
   */
  public Vertice getVerticeById(int id)
  {
      for(Vertice v : vertices)
      {
          if(id == v.id) return v;
      }
      return null;
  }
  public int getIdByVertice(int id)
  {
      int i = 0;
      for(Vertice v : vertices)
      {
          if(id == v.id) return i;
          i++;
      }
      return 0;
  }
  public void print()
  {
      System.out.println("[GRAFO] Vertices:");
      
      if(vertices.size() > 0)
      {
        for(Vertice v : vertices)
        {
            System.out.println(v.getDesc());
        }
      }
      
      System.out.println("[GRAFO] Arestas:");
      if(arestas.size() > 0)
      {
        for(Aresta a : arestas)
        {
          System.out.println(a.getDesc());
        }
      }
  }
  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VERTICES((short)1, "vertices"),
    ARESTAS((short)2, "arestas");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // VERTICES
          return VERTICES;
        case 2: // ARESTAS
          return ARESTAS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VERTICES, new org.apache.thrift.meta_data.FieldMetaData("vertices", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Vertice.class))));
    tmpMap.put(_Fields.ARESTAS, new org.apache.thrift.meta_data.FieldMetaData("arestas", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Aresta.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Grafo.class, metaDataMap);
  }

  public Grafo() {
  }

  public Grafo(
    java.util.List<Vertice> vertices,
    java.util.List<Aresta> arestas)
  {
    this();
    this.vertices = vertices;
    this.arestas = arestas;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Grafo(Grafo other) {
    if (other.isSetVertices()) {
      java.util.List<Vertice> __this__vertices = new java.util.ArrayList<Vertice>(other.vertices.size());
      for (Vertice other_element : other.vertices) {
        __this__vertices.add(new Vertice(other_element));
      }
      this.vertices = __this__vertices;
    }
    if (other.isSetArestas()) {
      java.util.List<Aresta> __this__arestas = new java.util.ArrayList<Aresta>(other.arestas.size());
      for (Aresta other_element : other.arestas) {
        __this__arestas.add(new Aresta(other_element));
      }
      this.arestas = __this__arestas;
    }
  }

  public Grafo deepCopy() {
    return new Grafo(this);
  }

  @Override
  public void clear() {
    this.vertices = null;
    this.arestas = null;
  }

  public int getVerticesSize() {
    return (this.vertices == null) ? 0 : this.vertices.size();
  }

  public java.util.Iterator<Vertice> getVerticesIterator() {
    return (this.vertices == null) ? null : this.vertices.iterator();
  }

  public void addToVertices(Vertice elem) {
    if (this.vertices == null) {
      this.vertices = new java.util.ArrayList<Vertice>();
    }
    this.vertices.add(elem);
  }

  public java.util.List<Vertice> getVertices() {
    return this.vertices;
  }

  public Grafo setVertices(java.util.List<Vertice> vertices) {
    this.vertices = vertices;
    return this;
  }

  public void unsetVertices() {
    this.vertices = null;
  }

  /** Returns true if field vertices is set (has been assigned a value) and false otherwise */
  public boolean isSetVertices() {
    return this.vertices != null;
  }

  public void setVerticesIsSet(boolean value) {
    if (!value) {
      this.vertices = null;
    }
  }

  public int getArestasSize() {
    return (this.arestas == null) ? 0 : this.arestas.size();
  }

  public java.util.Iterator<Aresta> getArestasIterator() {
    return (this.arestas == null) ? null : this.arestas.iterator();
  }

  public void addToArestas(Aresta elem) {
    if (this.arestas == null) {
      this.arestas = new java.util.ArrayList<Aresta>();
    }
    this.arestas.add(elem);
  }

  public java.util.List<Aresta> getArestas() {
    return this.arestas;
  }

  public Grafo setArestas(java.util.List<Aresta> arestas) {
    this.arestas = arestas;
    return this;
  }

  public void unsetArestas() {
    this.arestas = null;
  }

  /** Returns true if field arestas is set (has been assigned a value) and false otherwise */
  public boolean isSetArestas() {
    return this.arestas != null;
  }

  public void setArestasIsSet(boolean value) {
    if (!value) {
      this.arestas = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VERTICES:
      if (value == null) {
        unsetVertices();
      } else {
        setVertices((java.util.List<Vertice>)value);
      }
      break;

    case ARESTAS:
      if (value == null) {
        unsetArestas();
      } else {
        setArestas((java.util.List<Aresta>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VERTICES:
      return getVertices();

    case ARESTAS:
      return getArestas();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VERTICES:
      return isSetVertices();
    case ARESTAS:
      return isSetArestas();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Grafo)
      return this.equals((Grafo)that);
    return false;
  }

  public boolean equals(Grafo that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_vertices = true && this.isSetVertices();
    boolean that_present_vertices = true && that.isSetVertices();
    if (this_present_vertices || that_present_vertices) {
      if (!(this_present_vertices && that_present_vertices))
        return false;
      if (!this.vertices.equals(that.vertices))
        return false;
    }

    boolean this_present_arestas = true && this.isSetArestas();
    boolean that_present_arestas = true && that.isSetArestas();
    if (this_present_arestas || that_present_arestas) {
      if (!(this_present_arestas && that_present_arestas))
        return false;
      if (!this.arestas.equals(that.arestas))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVertices()) ? 131071 : 524287);
    if (isSetVertices())
      hashCode = hashCode * 8191 + vertices.hashCode();

    hashCode = hashCode * 8191 + ((isSetArestas()) ? 131071 : 524287);
    if (isSetArestas())
      hashCode = hashCode * 8191 + arestas.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Grafo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetVertices()).compareTo(other.isSetVertices());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVertices()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.vertices, other.vertices);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetArestas()).compareTo(other.isSetArestas());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetArestas()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.arestas, other.arestas);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Grafo(");
    boolean first = true;

    sb.append("vertices:");
    if (this.vertices == null) {
      sb.append("null");
    } else {
      sb.append(this.vertices);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("arestas:");
    if (this.arestas == null) {
      sb.append("null");
    } else {
      sb.append(this.arestas);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GrafoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GrafoStandardScheme getScheme() {
      return new GrafoStandardScheme();
    }
  }

  private static class GrafoStandardScheme extends org.apache.thrift.scheme.StandardScheme<Grafo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Grafo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VERTICES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.vertices = new java.util.ArrayList<Vertice>(_list0.size);
                Vertice _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = new Vertice();
                  _elem1.read(iprot);
                  struct.vertices.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setVerticesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ARESTAS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list3 = iprot.readListBegin();
                struct.arestas = new java.util.ArrayList<Aresta>(_list3.size);
                Aresta _elem4;
                for (int _i5 = 0; _i5 < _list3.size; ++_i5)
                {
                  _elem4 = new Aresta();
                  _elem4.read(iprot);
                  struct.arestas.add(_elem4);
                }
                iprot.readListEnd();
              }
              struct.setArestasIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Grafo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.vertices != null) {
        oprot.writeFieldBegin(VERTICES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.vertices.size()));
          for (Vertice _iter6 : struct.vertices)
          {
            _iter6.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.arestas != null) {
        oprot.writeFieldBegin(ARESTAS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.arestas.size()));
          for (Aresta _iter7 : struct.arestas)
          {
            _iter7.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GrafoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GrafoTupleScheme getScheme() {
      return new GrafoTupleScheme();
    }
  }

  private static class GrafoTupleScheme extends org.apache.thrift.scheme.TupleScheme<Grafo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Grafo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetVertices()) {
        optionals.set(0);
      }
      if (struct.isSetArestas()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetVertices()) {
        {
          oprot.writeI32(struct.vertices.size());
          for (Vertice _iter8 : struct.vertices)
          {
            _iter8.write(oprot);
          }
        }
      }
      if (struct.isSetArestas()) {
        {
          oprot.writeI32(struct.arestas.size());
          for (Aresta _iter9 : struct.arestas)
          {
            _iter9.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Grafo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list10 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.vertices = new java.util.ArrayList<Vertice>(_list10.size);
          Vertice _elem11;
          for (int _i12 = 0; _i12 < _list10.size; ++_i12)
          {
            _elem11 = new Vertice();
            _elem11.read(iprot);
            struct.vertices.add(_elem11);
          }
        }
        struct.setVerticesIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.arestas = new java.util.ArrayList<Aresta>(_list13.size);
          Aresta _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new Aresta();
            _elem14.read(iprot);
            struct.arestas.add(_elem14);
          }
        }
        struct.setArestasIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

