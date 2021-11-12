<template>
  <div class="panel-group">
    <el-card class="box-card">
      <div slot="header">
        <span class="title">快捷方式</span>
        <span class="right" @click="toShortcut">+</span>
      </div>
      <div
        v-for="shortcut in shortcutData"
        class="item"
        @click="handleJump(shortcut.path)"
        :style="{
          backgroundColor: shortcut.bgColor
        }"
      >
        <svg-icon :icon-class="shortcut.icon"/>
        <div class="desc">{{ shortcut.name }}</div>
      </div>
    </el-card>
  </div>
</template>

<script>
  import {shortcutList} from "@/api/system/shortcut";

  export default {
    data() {
      return {
        shortcutData: []
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        shortcutList({}).then(response => {
          this.shortcutData = response.data;
        });
      },
      handleJump(path) {
        this.$router.push(path)
      },
      toShortcut() {
        this.$router.push('/system/shortcut')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 10px;

    .el-card {
      margin-bottom: 16px;
    }

    /deep/ .box-card .el-card__header {
      padding: 0;

      .title {
        height: 55px;
        line-height: 55px;
        margin-left: 16px;
      }

      .right {
        float: right;
        margin-right: 12px;
        font-size: 48px;
        font-weight: 300;
        cursor: pointer;
      }
    }

    /deep/ .box-card .el-card__body {
      padding: 0;
      overflow-x: auto;

      .item {
        width: 100px;
        float: left;
        margin: 10px 20px;
        color: white;
        text-align: center;
        padding: 8px;
        border-radius: 8px;
        cursor: pointer;

        svg {
          font-size: 32px;
          margin-bottom: 12px;
        }
      }
    }
  }

</style>
