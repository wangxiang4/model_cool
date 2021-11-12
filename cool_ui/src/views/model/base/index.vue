<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--层级区域-->
      <el-col :span="6" :xs="24" >
        <div>
          <el-input
            v-model="bomName"
            placeholder="请输入标准名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div >
          <el-scrollbar style="height:488px;">
            <el-tree
               :data="bomOptions"
               :props="bomDefaultProps"
               :expand-on-click-node="false"
               :filter-node-method="bomFilterNode"
               ref="tree1"
               default-expand-all
               @node-click="bomHandleNodeClick">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="openBaseItem1(data)">
                  预览
                </el-button>
              </span>
            </span>
            </el-tree>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="6" :xs="24">
        <div>
          <el-input
            v-model="className"
            placeholder="请输入分类名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div>
          <el-scrollbar style="height:488px;">
          <el-tree
            :data="classOptions"
            :props="classDefaultProps"
            :expand-on-click-node="false"
            :filter-node-method="classFilterNode"
            ref="tree2"
            default-expand-all
            @node-click="classHandleNodeClick">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="openBaseItem1(data)">
                  预览
                </el-button>
              </span>
            </span>
          </el-tree>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="6" :xs="24">
        <div>
          <el-input
            v-model="typeName"
            placeholder="请输入型号名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div>
          <el-scrollbar style="height:488px;">
          <el-tree
            :data="typeOptions"
            :props="typeDefaultProps"
            :expand-on-click-node="false"
            :filter-node-method="typeFilterNode"
            ref="tree3"
            default-expand-all
            @node-click="typeHandleNodeClick">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="openBaseItem1(data)">
                  预览
                </el-button>
              </span>
            </span>
          </el-tree>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="6" :xs="24">
        <div>
          <el-input
            v-model="attributeName"
            placeholder="请输入属性名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div>
          <el-scrollbar style="height:488px;">
          <el-tree
            :data="attributeOptions"
            :props="attributeDefaultProps"
            :expand-on-click-node="false"
            :filter-node-method="attributeFilterNode"
            ref="tree4"
            default-expand-all
            @node-click="attributeHandleNodeClick"/>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import {InitData,listClass,listType,listAttribute} from "@/api/model/base"
import {handleTree} from "@/utils/cool";

export default {
  name: "ModelBase",
  data() {
    return{
      queryParams:{},
      //bom标准名称
      bomName: undefined,
      //bom标准名称树选项
      bomOptions: undefined,
      //分类名称
      className: undefined,
      //分类树选项
      classOptions: undefined,
      //型号名称
      typeName: undefined,
      //型号树选项
      typeOptions: undefined,
      //属性名称
      attributeName: undefined,
      //属性树选项
      attributeOptions: undefined,
      bomDefaultProps: {
        children: "children",
        label: "bomName"
      },
      classDefaultProps: {
        children: "children",
        label: "menuFirstName"
      },
      typeDefaultProps: {
        children: "children",
        label: "menuTwoName"
      },
      attributeDefaultProps: {
        children: "children",
        label: "attributeName"
      }
    }
  },
  computed: {},
  watch: {
    bomName(val) {
      this.$refs.tree1.filter(val);
    },
    className(val) {
      this.$refs.tree2.filter(val);
    },
    typeName(val) {
      this.$refs.tree3.filter(val);
    },
    attributeName(val) {
      this.$refs.tree4.filter(val);
    }
  },
  created() {
    this.InitData()
  },
  methods: {
    //筛选节点
    bomFilterNode(value, data) {
      if (!value) return true;
      return data.bomName.indexOf(value) !== -1;
    },
    classFilterNode(value, data) {
      if (!value) return true;
      return data.menuFirstName.indexOf(value) !== -1;
    },
    typeFilterNode(value, data) {
      if (!value) return true;
      return data.menuTwoName.indexOf(value) !== -1;
    },
    attributeFilterNode(value, data) {
      if (!value) return true;
      return data.attributeName.indexOf(value) !== -1;
    },
    bomHandleNodeClick(data) {
      this.typeOptions=[];
      this.attributeOptions=[];
      this.queryParams.bomId=data.bomId;
      this.listClass();
    },
    classHandleNodeClick(data) {
      this.attributeOptions=[];
      this.queryParams.typeId=data.menuFirstId;
      this.listType();
    },
    typeHandleNodeClick(data) {
      this.queryParams.attributeId=data.menuTwoId;
      this.listAttribute();
    },
    attributeHandleNodeClick(data) {
      if(!data.hasOwnProperty("children")){
        this.openBaseItem(data);
      }
    },
    InitData(){
      InitData().then(response=>{
        this.bomOptions=response.data.bomOptions;
        this.classOptions=response.data.classOptions;
        this.typeOptions=response.data.typeOptions;
        this.attributeOptions=handleTree(response.data.attributeOptions,"attributeId","attributeParentId",null,"root");
      })
    },
    //查询分类列表数据
    listClass(){
      listClass(this.queryParams).then(response=>{
        this.classOptions=response.data;
      })
    },
    //查询类型列表数据
    listType(){
      listType(this.queryParams).then(response=>{
        this.typeOptions=response.data;
      })
    },
    //查询属性列表数据
    listAttribute(){
      listAttribute(this.queryParams).then(response=>{
        this.attributeOptions=handleTree(response.data,"attributeId","attributeParentId",null,"root");
      })
    },
    //打开模具页面
    openBaseItem(node){
      this.$router.push({name:'BaseItem',query:{type:'attribute',id:node.attributeId}});
    }
  }
}

</script>

<style rel="stylesheet/scss" lang="scss">

.el-scrollbar{
  height: 100%;
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .el-scrollbar__bar{
    // 默认展示滚动条
    opacity: 1;
  }
  .el-scrollbar__thumb{
    // 改变滚动条颜色
    //background: #eabbbb;
  }
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}




</style>
