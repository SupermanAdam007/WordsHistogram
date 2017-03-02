;
; Easifier v4.2
;
; Created by Honza Zika (zika@avast.com)
;
; Overview: GUI for parsing and saving multistring detections
;
; Workflow:
;   - Creates GUI with njs.def parser
;   - User must insert Family and Name of the detection on the first and second line of input textarea
;   - User must select Type from combobox
;   - User must click Parse button
;   - Creates temp file with the detection in the same folder as this .au3 file is
;   - Activates Total Commander window
;   - Appends temp file to njs.def/ndyna.def in current folder
;   - Runs mscan.pl
;
; Version history:
;   4.2 (6 November 2013)
;     - All configurable variables are now loaded from easifier.cfg file
;     - Can be set to start in either bottom left or bottom right corner, should now work regardless of the screen resolution
;   4.1 (23 September 2013)
;     - Renamed to Easifier to better represent what this tool does
;     - Typing of command to TotalCommander now in one go - utilization of clipboard
;   4.0 (17 September 2013)
;     - Now does not need any specific file editor. The detection is saved to a temp file, then the file is appended to njs.def in current dir
;     - Preserves clipboard
;   3.2 (17 September 2013)
;     - Added support for Dyna detections
;       - Does not normalize whitespaces
;       - Outputs to ndyna.def
;       - mscan.bat still needs to be set up
;   3.1 (8 August 2013)
;     - Added automatic checkbit "IS_PDF" for JS:Pdfka detection.
;     - Now correctly handles ascii 1-32 characters (converts them to space first)
;   3.0 (22 July 2013)
;     - Added Ascii -> Hex switch
;       - If this is on, first converts input lines to hex
;       - This feature still produces ascstrings, and is intended to fight Van der Zande obfuscators that encode normal text to hex
;   2.9 (7 May 2013)
;     - Added autocomplete for the most used languages as well
;     - Now 1st line must contain language, second line family, third & on asciistrings
;   2.8 (17 March 2013)
;     - Minor tweak:
;       - Input window no longer flooded with multiple new lines, only one new line present all the time
;   2.7 (11 February 2013)
;     - Minor tweaks and improvements:
;       - Running mscan no longer depends on shortcut set in Total Commander
;       - Strips whitespace from first line (prevents trailing whitespaces to corrupt njs.def format)
;       - Waiting for njs.def file opened in notepad no longer depends on what the title looks like
;         (now works with both "njs.def - Notepad" and "njs - Notepad")
;       - When displaying error message because of wrong length of ascstring, displays current length as well
;   2.6 (8 February 2013)
;     - Added autocomplete for the most used families
;   2.5 (13 January 2013)
;     - Now checks if string does not end with a wildchar (?)
;   2.4 (9 January 2013)
;     - When pasting a multiline text from clipboard into input textarea, it is inserted as single line
;       - Replaces \r\n with a single space
;       - Replaces multiple space characters with a single space to increase readability of the code
;       - Pasting clipboard content to result textarea remains as expected
;   2.3 (28 December 2012)
;     - Replaces not allowed characters with question marks (allowed chars are ascii 32-127 excl. lowercase a-z)
;   2.2 (12 December 2012)
;     - Now adds "Agent" to name of detection even if first line includes colon (:)
;     - Now checks length of ascstring and raises error on too short (<4) or too long (>127) strings
;     - Now checks if position of wildchar (?) is not too close to start of the string
;     - Checks language and raises error on unkonwn language
;     - CTRL+A now properly works in both texareas
;   2.1 (11 December 2012)
;     - Checkbit "ISNOT_HTML" now automatically added to BV and AutoIt detections (rather than checkbox-controlled)
;   2.0 (28 November 2012)
;     - GUI now in Autoit rather than webpage
;   1.1 (24 November 2012)
;     - Rather than clicking, activate windows and press buttons by ids instead
;   1.0
;     - First version
;

; Read setting file
#include <easifier.cfg>

#include <GUIConstantsEx.au3>
#include <EditConstants.au3>
#include <WindowsConstants.au3>
#include <ComboConstants.au3>
#include <GUIEdit.au3>
#include <WinAPI.au3>

Opt("WinTitleMatchMode", 1) ;1=start, 2=subStr, 3=exact, 4=advanced, -1 to -4=Nocase

; Create GUI
$GUI_height = 335
$GUI_width = 800
$GUI_margin = 10
If $position = "bottomleft" Then
  $GUI_left = $GUI_margin
  $GUI_top  = @DesktopHeight - $GUI_height - $GUI_margin - 70
ElseIf $position = "bottomright" Then
  $GUI_left = @DesktopWidth - $GUI_width - $GUI_margin - 10
  $GUI_top  = @DesktopHeight - $GUI_height - $GUI_margin - 70
EndIf
$hGUI = GUICreate("Easifier", $GUI_width, $GUI_height, $GUI_left, $GUI_top)
GUISetFont(11, 400, 1, "Consolas")
GUISetState()	; Show window

; Create heading
$heading = GUICtrlCreateLabel("Easifier v4.2", 15, 15, 200)
GUICtrlSetFont($heading, 11, 800)	; bold

; Create heading
$heading = GUICtrlCreateLabel("©reated by Honza Zíka (zika@avast.com)", 220, 15, 450)

; Create textarea for inputting
$predefined_text = ""
$inputfield = GUICtrlCreateEdit($predefined_text, 10, 50, 780, 140, $ES_MULTILINE + $ES_WANTRETURN + $WS_VSCROLL)

; Create "Parse" button
$parsebutton = GUICtrlCreateButton("Parse!", 10, 200, 80, 30)

; Create Trj/Expl/Wrm/.. Combobox
$typecombo = GUICtrlCreateCombo("Trj  – Trojan", 100, 202, 140, 30, $CBS_DROPDOWNLIST)
GUICtrlSetData($typecombo, "Wrm  – Worm|Expl – Exploit|Drp  – Dropper|Joke – Joke|Adw  – Adware|Tool – Tool|Rtk  – Rootkit|Dialer – Dialer")

; Create "Copy Detection?" Checkbox
$copydetectioncheckbox = GUICtrlCreateCheckbox("Copy Detection", 250, 200, 140, 30)
GUICtrlSetState($copydetectioncheckbox, $GUI_CHECKED)

; Create "Ascii -> Hex" Checkbox
$asciitohexcheckbox = GUICtrlCreateCheckbox("Ascii → Hex", 400, 200, 150, 30)
GUICtrlSetState($asciitohexcheckbox, $GUI_UNCHECKED)

; Create result field
$predefined_text = "Welcome back!" & @CRLF & "If you are new here, please see source code or contact me at zika@avast.com."
$resultfield = GUICtrlCreateEdit($predefined_text, 10, 240, 780, 80, $ES_MULTILINE + $ES_WANTRETURN + $WS_VSCROLL)

; For CTRL+A to work: create dummy for accelerator key to activate, and set accelerators for ctrl+a
$hSelAll = GUICtrlCreateDummy()
$hInsert = GUICtrlCreateDummy()
Dim $AccelKeys[2][2]=[["^a", $hSelAll], ["^v", $hInsert]]
GUISetAccelerators($AccelKeys)

; Autocomplete in first line of input field
GUIRegisterMsg($WM_COMMAND, "Autocomplete")
Global $last_langlen = 0
Global $last_familylen = 0

; Main loop, waiting for events to occur
While 1
  Switch GUIGetMsg()
    Case $parsebutton
      $text = GUICtrlRead($inputfield)
      $text = StringStripWS($text, 3)
      $result = ""
      $chyba = ""
      $radky = StringSplit($text, @CRLF, 1)
      If ($radky[0] < 3) Then
        $chyba &= "Error: First line must contain language, second line family name, third & on ascii strings."
      Else

        ; Processing of language
        $jazyk = StringStripWS($radky[1], 3)
        $j = StringLower($jazyk)	; pro automaticky checkbit ISNOT_HTML
        If (NOT ($j=="als" or $j=="asp" or $j=="autoit" or $j=="bv" or $j=="dyna" or $j=="html" or $j=="inf" or $j=="ini" or $j=="irc" or $j=="js" or $j=="java" or $j=="lnk" or $j=="matlab" or $j=="macos" or $j=="nsis" or $j=="pdf" or $j=="php" or $j=="perl" or $j=="python" or $j=="rtf" or $j=="ruby" or $j=="sfx" or $j=="vbs" or $j=="xml")) Then
          $chyba &= "Error on line 1: Language not recognized." & @CRLF
        EndIf
  
        ; Processing of virus family
        $family = StringStripWS($radky[2], 3)
        If ($family == "") Then
          $family = "Agent"
          $radky[2] = "Agent"
        EndIf
        $f = StringLower($family)	; pro automaticky checkbit IS_PDF
        
        ; Processing of virus type
        $type = GUICtrlRead($typecombo)
        $type = StringRegExpReplace($type, "([A-Za-z]+).*", "$1")
  
        ; Creating 1st line of signature
        $result &= 'name "' & $jazyk & ':' & $family & '-* [' & $type & ']"' & @CRLF & '{' & @CRLF
  
  
        For $i = 3 to $radky[0]		; ascstring

          If (GUICtrlRead($asciitohexcheckbox) == $GUI_CHECKED) Then    ; first convert to hex
            
            $ascstring = AscToHex($radky[$i])
            
          Else      ; typical ascii string
          
            $ascstring = StringUpper($radky[$i])
            $ascstring = StringReplace($ascstring, '\', '\\')
            $ascstring = StringReplace($ascstring, '"', '\"')
    
            $ascstring = StringRegExpReplace($ascstring, "[\x01-\x20]", " ")   ; ascii 1-32 nahradit mezerou
            $ascstring = StringRegExpReplace($ascstring, "[^ -~]", "?")		; nahradit cokoli mimo ascii 32-126 (mezera az tilda) otaznikem
            If (NOT ($j == "dyna")) Then    ; v dyne se nenormalizujou mezery
              $ascstring = StringRegExpReplace($ascstring, "\s+", " ")
              $ascstring = StringRegExpReplace($ascstring, " (?![A-Z0-9])", "")	; smazat mezeru, pokud je pred nealfanumerickym znakem
              $ascstring = StringRegExpReplace($ascstring, "(?<![A-Z0-9]) ", "")	; smazat mezeru, pokud je za nealfanumerickym znakem
            EndIf
    
            While StringRight($ascstring, 1) == "?" ; ascstring must not end with question mark
              $ascstring = StringTrimRight($ascstring, 1)
            WEnd
          
          EndIf


          $ascstring_len = StringLen($ascstring)
          If ($ascstring_len > 127) Then
            $chyba &= "Error on line " & $i & ": Line is too long (" & $ascstring_len & " characters). Maximum length is 127 characters. Consider splitting it into multiple lines." & @CRLF
          EndIf
          If ($ascstring_len < 4) Then
            $chyba &= "Error on line " & $i & ": Line is too short (" & $ascstring_len & " characters). Minimum length is 4." & @CRLF
          EndIf
          If (StringInStr($ascstring, "?") > 0 AND StringInStr($ascstring, "?") < 5) Then
            $chyba &= "Error on line " & $i & ": Wildchar (?) used too early. At least 4 characters must preceed." & @CRLF
          EndIf
  
          $result &= @TAB & 'ascstring "' & $ascstring & '";' & @CRLF
        Next
  
        If ($j == "bv" OR $j == "autoit" OR $j == "ini" OR $j == "inf" OR $j == "perl" OR $j == "sfx") Then	; Vse musi byt lowercase!
          $result &= @CRLF & @TAB & 'checkbit "ISNOT_HTML";' & @CRLF
        EndIf
  
        If ($f == "pdfka") Then	; Vse musi byt lowercase!
          $result &= @CRLF & @TAB & 'checkbit "IS_PDF";' & @CRLF
        EndIf
  
        $result &= '}' & @CRLF & @CRLF
  
        If ($chyba == "") Then
          GUICtrlSetData($resultfield, $result)
          GUICtrlSetData($inputfield, $text & @CRLF)
  
          If (GUICtrlRead($copydetectioncheckbox) == $GUI_CHECKED) Then
            
            If ($dumpdir == "") Then    ; dumping in the current folder
              ; Create temporary file with the detection
              $file_name = @ScriptDir & "\easifier_out.dat"
              $file_handle = FileOpen($file_name, 2)
              FileWrite($file_handle, $result)
              FileClose($file_handle)
              
              ; Select Total Commander window
              WinActivate("Total Commander")
              
              ; Append the temp file to njs.def or ndyna.def
              If ($j == "dyna") Then
                $copy_command = "type " & $file_name & " >> " & $dumpfile_dyna
              Else
                $copy_command = "type " & $file_name & " >> " & $dumpfile_njs
                ; If we want to run mscan as well
                $copy_command &= " & mscan.bat"
              EndIf
              
              $clipbak = ClipGet()    ; backup clipboard
              $copy_command_start = StringLeft($copy_command, 1)  ; prvni pismenko
              $copy_command_rest =  StringMid ($copy_command, 2)  ; zbytek prikazu
              ClipPut($copy_command_rest)
              Send($copy_command_start)  ; napise prvni pismenko prikazu
              Send("^v")   ; ctrl+v      ; napise zbytek prikazu (ze schranky, takze rychle!)
              Send("{ENTER}")
              ClipPut($clipbak)     ; restore clipboard
              
            Else    ; dumping in a specified folder
              If ($j == "dyna") Then
                $dumpfile = $dumpfile_dyna
              Else
                $dumpfile = $dumpfile_njs
              EndIf
              $file_name = $dumpdir & $dumpfile
              $file_handle = FileOpen($file_name, 1) ; append
              FileWrite($file_handle, $result)
              FileClose($file_handle)

            EndIf ; Dumping
          EndIf ; Insertion of detection
        EndIf   ; if not error
      EndIf     ; if >= 3 lines in input field
      If ($chyba <> "") Then GUICtrlSetData($resultfield, $chyba)

    Case $hSelAll	; Fixes a bug in Windows with pressing CTRL+A in Edit Fields
      $hWnd = _WinAPI_GetFocus()
      $class = _WinAPI_GetClassName($hWnd)
      If $class = 'Edit' Then _GUICtrlEdit_SetSel($hWnd, 0, -1)

    Case $hInsert	; When CTRL+V is pressed, paste text as one line even if multiline text is in clipboard
      $hWnd = _WinAPI_GetFocus()
      $hinputfield = GUICtrlGetHandle($inputfield)
      $hresultfield = GUICtrlGetHandle($resultfield)
      
      If $hWnd = $hinputfield Then
        $text = ClipGet()
        $text = StringRegExpReplace($text, "\r\n", " ")
        $text = StringRegExpReplace($text, "\s+", " ")
        $text &= @CRLF
        GUICtrlSetData ($inputfield, $text, "1")
      ElseIf $hWnd = $hresultfield Then
        $text = ClipGet()
        GUICtrlSetData ($resultfield, $text, "1")
      EndIf

    Case $GUI_EVENT_CLOSE
      ExitLoop
  EndSwitch
WEnd

Func Autocomplete($hWnd, $msg, $wParam, $lParam)
  $IDFrom = BitAND($wParam, 0xFFFF)
  $iCode = BitShift($wParam, 16)
  If ($iCode = 768 and $IDFrom = 5) Then ; Text was changed in input field
    $text = GUICtrlRead($inputfield)
    $text = StringStripWS($text, 2)
    $radky = StringSplit($text, @CRLF, 1)
    $radka = ControlCommand($hGUI, "", $inputfield, "GetCurrentLine", "")
    If ($radka == 1) Then
      $curr_lang = $radky[1]
      $curr_langlen = StringLen($curr_lang)
      $zname_lang_arr = StringSplit($zname_lang, "|")
      $curr_navrh = ""
      
      For $i = 0 to UBound($zname_lang_arr) - 1
    	  If (StringInStr($zname_lang_arr[$i], $curr_lang) = 1 and $zname_lang_arr[$i] <> $curr_lang) Then
          $curr_navrh = $zname_lang_arr[$i]
          ExitLoop
        EndIf
      Next
      
      If ($curr_navrh <> "" and $curr_langlen > $last_langlen) Then
        $radky[1] = $curr_navrh
        $result = ""
    	  For $i = 1 to $radky[0]
          $result &= $radky[$i] & @CRLF
        Next
  
        $selection_startpos = $curr_langlen
        $selection_endpos   = StringLen($curr_navrh)
  
        GUICtrlSetData($inputfield, $result)
   	    _GUICtrlEdit_SetSel($inputfield, $selection_startpos, $selection_endpos)
      EndIf
  
      $last_langlen = $curr_langlen
      
      
    ElseIf ($radka == 2 and $radky[0]>1) Then
      $curr_rodina = $radky[2]
      $curr_familylen = StringLen($curr_rodina)
      $zname_family_arr = StringSplit($zname_family, "|")
      $curr_navrh = ""
      
      For $i = 0 to UBound($zname_family_arr) - 1
    	  If (StringInStr($zname_family_arr[$i], $curr_rodina) = 1 and $zname_family_arr[$i] <> $curr_rodina) Then
          $curr_navrh = $zname_family_arr[$i]
          ExitLoop
        EndIf
      Next
      
      If ($curr_navrh <> "" and $curr_familylen > $last_familylen) Then
        $radky[2] = $curr_navrh
        $result = ""
    	  For $i = 1 to $radky[0]
          $result &= $radky[$i] & @CRLF
        Next
  
        $selection_startpos = StringLen($radky[1]) + 2 + $curr_familylen  ; 2 = @CRLF
        $selection_endpos   = $selection_startpos + StringLen($curr_navrh) - $curr_familylen
  
        GUICtrlSetData($inputfield, $result)
   	    _GUICtrlEdit_SetSel($inputfield, $selection_startpos, $selection_endpos)
      EndIf
  
      $last_familylen = $curr_familylen
    EndIf
  EndIf
;  Return $GUI_RUNDEFMSG
EndFunc ;==>Autocomplete()

Func AscToHex($string)
  $chararray = StringSplit($string, "")
  $outstr = ""
  For $i = 1 To $chararray[0]
    $outstr &= Hex(Asc($chararray[$i]), 2)
  Next;
  Return $outstr
EndFunc ;==>AscToHex()
