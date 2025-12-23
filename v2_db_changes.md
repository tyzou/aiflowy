

- 修改表名：所有的 tb_ai_ 开头的表前缀修改为 tb_
- 修改表名：tb_ai_bot_conversation_message --> tb_bot_conversation
- 修改字段：tb_ai_bot_conversation_message.session_id 修改为 id
- 删除字段：删除 tb_ai_bot_message.is_external_msg 字段
- 修改字段：tb_ai_bot_message.session_id 修改为 conversation_id
- 删除表名：tb_ai_chat_message 和 tb_ai_chat_topic
- 修改表名：tb_ai_llm 为 tb_model
- 修改表名：tb_ai_llm_provider 为 tb_model_provider
- 修改表名：tb_ai_plugin_category_relation ---> tb_plugin_category_mapping
- 修改表名：tb_ai_knowledge 表名为 tb_document_collection（文档集合，知识库）
- 修改表名：tb_ai_resource 修改为 tb_resource，并添加 tb_resource_category 分类表及其相关功能
- 修改表名：tb_sys_api_key_resource_permission ---> tb_sys_api_key_resource
  tb_sys_api_key_resource_permission_relationship ----> tb_sys_api_key_resource_mapping
- 删除表：tb_sys_token （iframe 嵌入用 Token 表）


- 修改表名：tb_workflow_exec_record ---> tb_workflow_exec_result
- 修改表名：tb_workflow_record_step ---> tb_workflow_exec_step


- 修改表名： tb_bot_knowledge --->  tb_bot_document_collection

- 修改字段：tb_model.llm_endpoint ---> endpoint
  tb_model.llm_model ---> model_name
  tb_model.llm_api_key ---> api_key
  tb_model.llm_extra_config ---> extra_config


- 新增字段：tb_model.request_path（请求路径）
- 修改字段：tb_model_provider.end_point ---> endpoint


- 修改表名：tb_bot_plugins  ---> tb_bot_plugin
- 修改表名：tb_bot_llm ---> tb_bot_model

- 修改表名：tb_plugin_categories ---->  tb_plugin_category
- 修改表名：tb_plugin_tool  ---->  tb_plugin_item
- 修改表名：tb_datacenter_table_fields  ---->  tb_datacenter_table_field


- 字段修改：tb_bot.llm_id ---> model_id
- 字段修改：tb_bot.llm_options ---> model_options
- 字段修改：tb_bot_document_collection.knowledge_id ---> document_collection_id
- 字段修改：tb_document_chunk.knowledge_id ---> document_collection_id
- 字段修改：tb_bot_model.llm_id ---> model_id
- 字段修改：tb_bot_plugin.plugin_tool_id ---> plugin_item_id
- 字段修改：tb_document.knowledge_id ---> collection_id
- 字段修改：tb_document_collection.vector_embed_llm_id ---> vector_embed_model_id

