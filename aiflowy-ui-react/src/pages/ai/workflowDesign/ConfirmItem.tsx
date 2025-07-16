import React from 'react'
import {Checkbox, Radio} from "antd";

export type ConfirmItemProps = {
    selectionDataType: string,
    selectionMode: string,
    selectionData: any[]
}

export const ConfirmItem: React.FC<ConfirmItemProps> = ({selectionDataType, selectionMode, selectionData}) => {

    /*const T = [ // dataType
                            { label: "文字", value: "text" },
                            { label: "图片", value: "image" },
                            { label: "视频", value: "video" },
                            { label: "音频", value: "audio" },
                            { label: "文件", value: "file" },
                            { label: "其他", value: "other" }
                        ], x = [ // mode
                            { label: "单选", value: "single" },
                            { label: "多选", value: "multiple" }
                        ];*/
    const renderSelectionComponent = (dataType:any,mode:any,selectionData:any) => {

        if (dataType === "text") {
            if (mode === "single") {
                return (
                    <Radio.Group>
                        {selectionData.map((option: any, index: number) => (
                            <Radio key={index} value={option}>
                                {option}
                            </Radio>
                        ))}
                    </Radio.Group>
                );
            }
            if (mode === "multiple") {
                return (
                    <Checkbox.Group>
                        {selectionData.map((option: any, index: number) => (
                            <Checkbox key={index} value={option}>
                                {option}
                            </Checkbox>
                        ))}
                    </Checkbox.Group>
                );
            }
        }
    }
    
    return (
        <>
            {renderSelectionComponent(selectionDataType,selectionMode,selectionData)}
        </>
    )
}