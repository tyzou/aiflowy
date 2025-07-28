import React, {useEffect, useState} from 'react'
import {useLocation} from "react-router-dom";
import {
    Button,
    Col,
    GetProp,
    Menu,
    message,
    Modal,
    Popconfirm,
    Row,
    Space,
    Spin,
    Table,
    Tooltip,
    UploadProps
} from "antd";
import {useLayout} from "../../../hooks/useLayout.tsx";
import css from './aitable.module.css'
import {
    EditOutlined,
    LeftOutlined,
    PlusOutlined,
    ReloadOutlined,
    RestOutlined, ToTopOutlined,
} from "@ant-design/icons";
import tableIcon from '../../../assets/table2x.png'
import {useGetManual, usePostFile, usePostManual} from "../../../hooks/useApis.ts";
import {DataSave} from "./DataSave.tsx";
import Dragger from "antd/es/upload/Dragger";
import uploadIcon from '../../../assets/upload.png'
import {UploadFile} from "antd/es/upload";

export const DatacenterTableDetail: React.FC = () => {
    const location = useLocation();
    const {tableId} = location.state || {};

    const {loading: detailLoading, doGet: getDetailInfo} = useGetManual('/api/v1/datacenterTable/detailInfo')
    const {loading: dataLoading, doGet: getTablePageData} = useGetManual('/api/v1/datacenterTable/getPageData')
    const {doGet: getHeaders} = useGetManual('/api/v1/datacenterTable/getHeaders')

    const [tableInfo, setTableInfo] = useState<any>(null)

    const [tableDataList, setTableDataList] = useState<any[]>([])

    const {loading: removeLoading, doPost: removeRecord} = usePostManual('/api/v1/datacenterTable/removeValue')

    useEffect(() => {
        if (tableId) {
            getDetailInfo({
                params: {
                    tableId
                }
            }).then(res => {
                setTableInfo(res.data.data)
                setFieldList(res.data.data.fields)
            })
            getHeaders({
                params: {
                    tableId
                }
            }).then(res => {
                const columns = res.data.data
                const dataCols: any[] = []
                for (const column of columns) {
                    const fieldType = column.fieldType
                    dataCols.push({
                        ...column,
                        render: (text:any) => {
                            if (fieldType === 3) {
                                return (
                                    <div>{text}</div>
                                )
                            }
                            if (fieldType === 5) {
                                return (
                                    <div>{1 ===  text ? "是" : "否"}</div>
                                )
                            }
                            return (
                                <div>{text}</div>
                            )
                        }
                    })
                }
                dataCols.push({
                    key:"action",
                    title: "操作",
                    render: (_: any, data: any) => {
                        return (
                            <Space size="middle">

                                <a onClick={() => {
                                    setCurrentData(data)
                                    setDataModal(true)
                                }}><EditOutlined/> 编辑 </a>

                                <Popconfirm
                                    title="确定删除？"
                                    description="您确定要删除这条数据吗？"
                                    okButtonProps={{loading: removeLoading}}
                                    onConfirm={async () => {
                                        await removeRecord({
                                            params: {
                                                tableId,
                                                id: data.id
                                            }
                                        })
                                        getTableData()
                                    }}
                                    okText="确定"
                                    cancelText="取消"
                                >
                                    <a style={{color: "#FF4D4D"}}> <RestOutlined/> 删除 </a>
                                </Popconfirm>
                            </Space>
                        )
                    }
                })
                setDataColumns(dataCols)
                getTableData()
            })
        } else {
            message.warning("tableId非法")
        }
    }, [])

    const pageInfo = {
        pageNumber: 1,
        pageSize: 10,
    }

    const [pageParams, setPageParams] = useState(pageInfo)
    const [total, setTotal] = useState(0)
    useEffect(() => {
        getTableData()
    },[pageParams])

    const getTableData = () => {
        getTablePageData({
            params: {
                tableId,
                ...pageParams
            }
        }).then(res => {
            setTableDataList(res.data.data.records)
            setTotal(res.data.data.totalRow)
        })
    }

    const items: any[] = [
        {key: '1', label: '表结构'},
        {key: '2', label: '数据'},
    ];

    const {setOptions} = useLayout();
    useEffect(() => {
        if (tableId) {
            setOptions({
                showBreadcrumb: true,
                breadcrumbs: [
                    {title: '首页'},
                    {title: 'AI能力'},
                    {title: '数据中枢'},
                    {title: '详情'},
                ]
            })
        }
        return () => {
            setOptions({
                showBreadcrumb: true,
                breadcrumbs: [],
                breadcrumbRightEl: null
            })
        }
    }, [tableId])

    const [activeKey, setActiveKey] = useState('1');
    const onClick = (e: any) => {
        setActiveKey(e.key)
    };

    const [fieldList, setFieldList] = useState<any[]>([])

    const fieldTypeConvert = {
        1: "String",
        2: "Integer",
        3: "Time",
        4: "Number",
        5: "Boolean",
    }

    const fieldColumns = [
        {
            dataIndex: "fieldName",
            title: "字段名称",
            key: "fieldName",
        },

        {
            dataIndex: "fieldDesc",
            title: "字段描述",
            key: "fieldDesc",
        },

        {
            dataIndex: "fieldType",
            title: "字段类型",
            key: "fieldType",
            render: (_: any, data: any) => {
                return fieldTypeConvert[data.fieldType as keyof typeof fieldTypeConvert] || '未知类型';
            }
        },

        {
            dataIndex: "required",
            title: "是否必填",
            key: "required",
            render: (t: any) => {
                return t === 1 ? "是" : "否"
            }
        },
    ]

    const [dataColumns, setDataColumns] = useState<any[]>([])

    const [currentData, setCurrentData] = useState<any>(null)
    const [dataModal, setDataModal] = useState(false)

    const [importModalOpen, setImportModalOpen] = useState(false)

    const [fileList, setFileList] = useState<UploadFile[]>([]);

    const props: UploadProps = {
        name: 'file',
        multiple: false,
        accept: ".xlsx,.xls,.csv",
        action: 'https://660d2bd96ddfa2943b33731c.mockapi.io/api/upload',
        beforeUpload: (file) => {
            setFileList([...fileList, file]);
            return false;
        },
        onRemove: (file) => {
            const index = fileList.indexOf(file);
            const newFileList = fileList.slice();
            newFileList.splice(index, 1);
            setFileList(newFileList);
        },
        onDrop(e) {
            console.log('Dropped files', e.dataTransfer.files);
        },
        fileList: fileList,
    };

    type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

    const {loading: importLoading, doPost: doPostFile} = usePostFile('/api/v1/datacenterTable/importData')

    const handleUpload = () => {
        const formData = new FormData();
        formData.append('file', fileList[0] as FileType);
        formData.append('tableId', tableId);
        doPostFile({
            data: formData,
        }).then(res => {
            if (res.data.errorCode === 0) {
                message.success(`导入成功`);
                getTableData()
                setImportModalOpen(false)
                setFileList([])
            } else {
                message.error(res.data.message);
            }
        })
    };

    const {doGet: getTemplate} = useGetManual('/api/v1/datacenterTable/getTemplate')
    const [divLoading, setDivLoading] = useState(false)

    return (
        <div style={{padding: "24px"}}>
            <DataSave
                tableId={tableId}
                formItems={dataColumns}
                entity={currentData}
                open={dataModal}
                ok={() => {
                    message.success("保存成功")
                    setDataModal(false)
                    getTableData()
                }}
                cancel={() => {
                    setDataModal(false)
                }}

            />
            <Modal
                open={importModalOpen}
                title={"批量导入"}
                width={530}
                confirmLoading={importLoading}
                onCancel={() => {
                    setImportModalOpen(false)
                    setFileList([])
                }}
                onOk={() => {
                    handleUpload()
                }}
            >
                <Dragger {...props} style={{backgroundColor: "#fff",marginTop: "10px",marginBottom: "5px"}}>
                    <div style={{padding: "39px 50px 69px 50px"}}>
                    <p className="ant-upload-drag-icon">
                        <img style={{width: "48px", height: "48px"}} src={uploadIcon} alt={""} />
                    </p>
                    <p className="ant-upload-text">点击或将文件拖拽到这里上传</p>
                    <p className="ant-upload-hint" style={{fontSize: "13px"}}>
                        上传一份Excel文档，文件大小限制10MB以内。
                    </p>
                    </div>
                </Dragger>
                <Spin spinning={divLoading}>
                    <a onClick={() => {
                        setDivLoading(true)
                        getTemplate({
                            params: {
                                tableId: tableId
                            },
                            responseType: "blob"
                        }).then(response => {
                            setDivLoading(false)
                            const finalFilename = "导入模板.xlsx";
                            // @ts-ignore
                            const blob = new Blob([response.data]);
                            const downloadUrl = window.URL.createObjectURL(blob);
                            const link = document.createElement('a');
                            link.href = downloadUrl;
                            link.setAttribute('download', finalFilename);
                            document.body.appendChild(link);
                            link.click();
                            // 清理
                            setTimeout(() => {
                                document.body.removeChild(link);
                                window.URL.revokeObjectURL(downloadUrl);
                            }, 100);
                        })
                    }}>下载模板</a>
                </Spin>
            </Modal>
            <Spin spinning={detailLoading}>
                <Row>
                    <Col span={24}>
                        <div className={css.detailHeader}>
                            <div className={css.iconContainer}>
                                <LeftOutlined className={css.backIcon} onClick={() => {
                                    history.back()
                                }}/>
                                <img src={tableIcon} className={css.iconStyle}/>
                                <div className={css.headerTableInfo}>
                                    <div className={css.tableName}>
                                        {tableInfo?.tableName}
                                    </div>
                                    <div className={css.tableDesc}>
                                        {tableInfo?.tableDesc}
                                    </div>
                                </div>
                            </div>
                            <div className={css.btnContainer}>
                                <Space>
                                    {activeKey === '2' && <Button type={"primary"} onClick={() => {
                                        setDataModal(true)
                                        setCurrentData(null)
                                    }}><PlusOutlined/>增加行</Button>}
                                    {activeKey === '2' && <Button type={"primary"} onClick={() => {
                                        setImportModalOpen(true)
                                    }}>
                                        <ToTopOutlined/>批量导入
                                    </Button>}
                                </Space>
                            </div>
                        </div>
                    </Col>
                    <Col span={6} className={css.menuContainer}>
                        <Menu
                            style={{borderInlineEnd: 'none'}}
                            onClick={onClick}
                            defaultSelectedKeys={['1']}
                            mode="inline"
                            items={items}
                        />
                    </Col>
                    <Col span={18}>
                        {activeKey === '1' &&
                            <div className={css.tableContainer}>
                                <Table
                                    rowKey={"id"}
                                    dataSource={fieldList}
                                    columns={fieldColumns}
                                    pagination={false}
                                />
                            </div>
                        }
                        {activeKey === '2' &&
                            <div className={css.tableContainer}>
                                <div style={{
                                    display: "flex",
                                    justifyContent: "flex-end",
                                    marginBottom: "10px",
                                    marginRight: "5px",
                                    marginTop: "-10px"
                                }}>
                                    <Space align={"center"} size={"middle"}>
                                        <Tooltip placement="top" title="刷新">
                                            <ReloadOutlined onClick={() => {
                                                getTableData()
                                            }}/>
                                        </Tooltip>
                                    </Space>
                                </div>
                                <Table
                                    loading={dataLoading}
                                    rowKey={"id"}
                                    dataSource={tableDataList}
                                    columns={dataColumns}
                                    onChange={(pagination, _) => {
                                        setPageParams({
                                            pageNumber: pagination.current || pageInfo.pageNumber,
                                            pageSize: pagination.pageSize || pageInfo.pageSize,
                                        })
                                    }}
                                    pagination={
                                        {
                                            position: ["bottomCenter"],
                                            pageSize: pageParams.pageSize,
                                            showQuickJumper: true,
                                            current: pageParams.pageNumber,
                                            total: total || 0,
                                            showTotal: (total) => `共 ${total} 条数据`,
                                        }
                                    }
                                />
                            </div>
                        }
                    </Col>
                </Row>
            </Spin>
        </div>
    )
};

export default {
    path: "/datacenter/tableDetail",
    element: DatacenterTableDetail
};