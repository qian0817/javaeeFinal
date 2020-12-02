import BraftEditor, {BraftEditorProps, BuiltInControlType} from "braft-editor";
import React from "react";

const Editor = (props: BraftEditorProps) => {
    const excludeControls: BuiltInControlType[] = [
        "font-size", "font-family", "line-height", "letter-spacing",
        'text-color', 'bold', 'italic', 'underline', 'strike-through',
        'superscript', 'subscript', 'text-indent', 'text-align', 'hr',
        'clear'
    ]
    return (
        <BraftEditor
            excludeControls={excludeControls}
            // controls={control}
            {...props}/>
    )
}

export default Editor;